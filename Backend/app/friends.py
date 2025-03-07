import jwt
from flask import Blueprint, jsonify, request, current_app
from .db import run_query
from .db import run_query, connect_to_db

friends_bp = Blueprint('friends', __name__)

def get_current_user():
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        return None
    token = auth_header[len("Bearer "):]
    try:
        payload = jwt.decode(token, current_app.config["JWT_SECRET"], algorithms=[current_app.config["JWT_ALGORITHM"]])
        return payload.get("user_id")
    except Exception as e:
        current_app.logger.error(f"JWT decoding error: {e}")
        return None

@friends_bp.route('/friend/remove', methods=['POST'])
def remove_friend():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    data = request.get_json()
    friend_id = data.get("friend_id")
    if not friend_id:
        return jsonify({"success": False, "error": "friend_id is required"}), 400

    cxn = None
    try:
        cxn = connect_to_db()
        curs = cxn.cursor()
        curs.execute(
            """
            DELETE FROM friendships 
            WHERE (user_id = %s AND friend_id = %s)
               OR (user_id = %s AND friend_id = %s)
            """,
            (user_id, friend_id, friend_id, user_id)
        )
        cxn.commit()
        return jsonify({"success": True, "message": "Friend removed successfully"}), 200
    except Exception as e:
        current_app.logger.error(f"Error in remove_friend: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500
    finally:
        if cxn:
            cxn.close()


@friends_bp.route('/friend/search', methods=['GET'])
def search_friend():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    query = request.args.get("q")
    if not query:
        return jsonify({"success": False, "error": "Query parameter 'q' is required"}), 400

    try:
        users = run_query(
            "SELECT user_id, username FROM users WHERE username ILIKE %s AND user_id != %s",
            ("%" + query + "%", user_id)
        )
        return jsonify({"success": True, "users": users}), 200
    except Exception as e:
        current_app.logger.error(f"Error in search_friend: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500

@friends_bp.route('/friend/list', methods=['GET'])
def list_friends():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    try:
        friends = run_query(
            """
            SELECT u.user_id, u.username FROM users u
            WHERE u.user_id IN (
                SELECT CASE
                    WHEN %s = user_id THEN friend_id
                    ELSE user_id
                END AS friend_id
                FROM friendships
                WHERE %s IN (user_id, friend_id)
            )
            """,
            (user_id, user_id)
        )
        return jsonify({"success": True, "friends": friends}), 200
    except Exception as e:
        current_app.logger.error(f"Error in list_friends: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500

@friends_bp.route('/friend/requests', methods=['GET'])
def list_friend_requests():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    try:
        requests_list = run_query(
            """
            SELECT u.user_id, u.username FROM users u
            JOIN friend_requests fr ON u.user_id = fr.sender_id
            WHERE fr.receiver_id = %s AND fr.status = 'pending'
            """,
            (user_id,)
        )
        return jsonify({"success": True, "requests": requests_list}), 200
    except Exception as e:
        current_app.logger.error(f"Error in list_friend_requests: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500

@friends_bp.route('/friend/request', methods=['POST'])
def handle_friend_request():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    data = request.get_json()
    action = data.get("action")
    sender_id = int(data.get("sender_id"))  
    if not action or not sender_id:
        return jsonify({"success": False, "error": "Both 'action' and 'sender_id' are required"}), 400

    try:
        if action.lower() == "approve":
            run_query(
                "DELETE FROM friend_requests WHERE sender_id = %s AND receiver_id = %s",
                (sender_id, user_id),
                fetch=False,
                commit=True
            )
            user_min = min(user_id, sender_id)
            user_max = max(user_id, sender_id)
            run_query(
                "INSERT INTO friendships (user_id, friend_id) VALUES (%s, %s)",
                (user_min, user_max),
                fetch=False,
                commit=True
            )
            return jsonify({"success": True, "message": "Friend request approved"}), 200
        elif action.lower() == "deny":
            run_query(
                "DELETE FROM friend_requests WHERE sender_id = %s AND receiver_id = %s",
                (sender_id, user_id),
                fetch=False,
                commit=True
            )
            return jsonify({"success": True, "message": "Friend request denied"}), 200
        else:
            return jsonify({"success": False, "error": "Invalid action"}), 400
    except Exception as e:
        current_app.logger.error(f"Error in handle_friend_request: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500
