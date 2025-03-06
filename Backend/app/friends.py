import jwt
from flask import Blueprint, jsonify, request, current_app
from .db import run_query

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

    try:
        run_query(
            "DELETE FROM friends WHERE (user_id = %s AND friend_id = %s) OR (user_id = %s AND friend_id = %s)",
            (user_id, friend_id, friend_id, user_id),
            fetch=False,
            commit=True
        )
        return jsonify({"success": True, "message": "Friend removed successfully"}), 200
    except Exception as e:
        current_app.logger.error(f"Error in remove_friend: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500

