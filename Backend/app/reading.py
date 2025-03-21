import jwt
from flask import Blueprint, jsonify, request, current_app
from .db import run_query
from .friends import get_current_user
from .books import fetch_from_google

reading_bp = Blueprint('reading', __name__)

@reading_bp.route('/reading/group/join', methods=['POST'])
def join_reading_group():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    data = request.get_json()
    group_id = data.get("group_id")
    if not group_id:
        return jsonify({"success": False, "error": "group_id is required"}), 400
    try:
        run_query(
            "INSERT INTO reading_group_members (group_id, user_id, role) VALUES (%s, %s, %s)",
            (group_id, user_id, 'member'), 
            fetch=False, 
            commit=True
        )
        return jsonify({"success": True, "message": "Reading group joined"}), 200
    except Exception as e:
        current_app.logger.error(f"Error in join_reading_group: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500


@reading_bp.route('/reading/group/recommend', methods=['POST'])
def recommend_book_to_group():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    data = request.get_json()
    group_id = data.get("group_id")
    external_id = data.get("external_id")
    if not group_id or not external_id:
        return jsonify({"success": False, "error": "group_id and external_id required"}), 400
    
    try:
        result = run_query(
            "SELECT user_id FROM reading_group_members WHERE group_id = %s AND user_id = %s",
            (group_id, user_id)
        )
        if not result or result[0]['user_id'] != user_id:
            return jsonify({"success": False, "error": "Not a member of this group"}), 403

        run_query(
            "INSERT INTO reading_list (group_id, external_id, suggested_by) VALUES (%s, %s, %s)",
            (group_id, external_id, user_id),
            fetch=False,
            commit=True
        )
        return jsonify({"success": True, "message": "Book recommended to group"}), 200
    except Exception as e:
        current_app.logger.error(f"Error in recommend_book_to_group: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500


@reading_bp.route('/reading/group/handle_recommendation', methods=['POST'])
def handle_book_recommendation():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401
    
    data = request.get_json()
    group_id = data.get("group_id")
    external_id = data.get("external_id")
    action = data.get("action")  # should be 'approve' or 'deny'
    if not action or not group_id or not external_id:
        return jsonify({"success": False, "error": "group_id, external_id and action are required"}), 400
    
    try:
        if action.lower() not in ["approve", "deny"]:
            return jsonify({"success": False, "error": "Invalid action"}), 400

        admin_check = run_query(
            "SELECT user_id FROM reading_group_members WHERE group_id = %s AND user_id = %s AND role = 'admin'",
            (group_id, user_id)
        )
        if not admin_check or admin_check[0]['user_id'] != user_id:
            return jsonify({"success": False, "error": "Not authorized"}), 403

        if action.lower() == "approve":
            run_query(
                "UPDATE reading_list SET status = %s WHERE group_id = %s AND external_id = %s",
                ("approve", group_id, external_id),
                fetch=False,
                commit=True
            )
        else:  
            run_query(
                "DELETE FROM reading_list WHERE group_id = %s AND external_id = %s",
                (group_id, external_id),
                fetch=False,
                commit=True
            )
        return jsonify({"success": True, "message": f"Recommendation {action.lower()}"}), 200
    except Exception as e:
        current_app.logger.error(f"Error in handle_book_recommendation: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500


@reading_bp.route('/reading/group/search', methods=['GET'])
def search_groups():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401
    
    group_name = request.args.get("q")
    if not group_name:
        return jsonify({"success": False, "error": "Query parameter 'q' is required"}), 400

    try:
        query_str = f"%{group_name}%"
        groups = run_query(
            "SELECT group_id, group_name, created_by FROM reading_groups WHERE group_name ILIKE %s",
            (query_str,)
        )
        return jsonify({"success": True, "groups": groups}), 200
    except Exception as e:
        current_app.logger.error(f"Error in search_groups: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500


@reading_bp.route('/reading/group/promote', methods=['POST'])
def promote_group_member():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    data = request.get_json()
    group_id = data.get("group_id")
    member_id = data.get("member_id")
    if not group_id or not member_id:
        return jsonify({"success": False, "error": "group_id and member_id are required"}), 400

    try:
        admin_check = run_query(
            "SELECT user_id FROM reading_group_members WHERE group_id = %s AND user_id = %s AND role = 'admin'",
            (group_id, user_id)
        )
        if not admin_check or admin_check[0]['user_id'] != user_id:
            return jsonify({"success": False, "error": "Not authorized"}), 403
        
        run_query(
            "UPDATE reading_group_members SET role = 'admin' WHERE user_id = %s AND group_id = %s",
            (member_id, group_id),
            fetch=False,
            commit=True
        )
        return jsonify({"success": True, "message": "Member promoted to admin"}), 200
    except Exception as e:
        current_app.logger.error(f"Error in promote_group_member: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500


@reading_bp.route('/reading/group/create', methods=['POST'])
def create_group():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    data = request.get_json()
    group_name = data.get("group_name")
    if not group_name:
        return jsonify({"success": False, "error": "group_name is required"}), 400

    try:
        run_query(
            "INSERT INTO reading_groups (group_name, created_by) VALUES (%s, %s)",
            (group_name, user_id),
            fetch=False,
            commit=True
        )
        new_group = run_query(
            "SELECT group_id FROM reading_groups WHERE group_name = %s AND created_by = %s ORDER BY group_id DESC LIMIT 1",
            (group_name, user_id)
        )
        new_group_id = new_group[0]['group_id'] if new_group else None

        if new_group_id:
            run_query(
                "INSERT INTO reading_group_members (group_id, user_id, role) VALUES (%s, %s, 'admin')",
                (new_group_id, user_id),
                fetch=False,
                commit=True
            )

        return jsonify({"success": True, "message": "Reading group created", "group_id": new_group_id}), 201
    except Exception as e:
        current_app.logger.error(f"Error in create_group: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500

@reading_bp.route('/reading/group/list', methods=['GET'])
def list_user_groups():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    try:
        groups = run_query(
            """
            SELECT rg.group_id, rg.group_name, rg.created_by, 
                   rgm.role, rgm.joined_at
            FROM reading_group_members rgm
            JOIN reading_groups rg ON rgm.group_id = rg.group_id
            WHERE rgm.user_id = %s
            """,
            (user_id,)
        )
        return jsonify({"success": True, "groups": groups}), 200

    except Exception as e:
        current_app.logger.error(f"Error in list_user_groups: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500

@reading_bp.route('/reading/group/recommend/list', methods=['GET'])
def list_recommendations():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    group_id = request.args.get("group_id")
    if not group_id:
        group_id = request.headers.get("group_id")

    try:
        query = """
            SELECT rl.group_id, rl.external_id, rl.status, rl.suggested_by
            FROM reading_list rl
            JOIN reading_group_members rgm ON rl.group_id = rgm.group_id
            WHERE rgm.user_id = %s
        """
        params = [user_id]
        if group_id:
            query += " AND rl.group_id = %s"
            params.append(group_id)

        recs = run_query(query, tuple(params))
        if not recs:
            return jsonify({"success": True, "items": []}), 200

        results = fetch_from_google(
            recs,
            extra_fields=["status", "suggested_by"]
        )

        return jsonify({"success": True, "items": results}), 200

    except Exception as e:
        current_app.logger.error(f"Error in list_recommendations: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500

@reading_bp.route('/reading/group/members', methods=['GET'])
def list_group_members():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    group_id = request.args.get("q")
    if not group_id:
        return jsonify({"success": False, "error": "q is required"}), 400

    try:
        query = """
            SELECT u.user_id, u.username
            FROM reading_group_members rgm
            JOIN users u ON rgm.user_id = u.user_id
            WHERE rgm.group_id = %s
        """
        members = run_query(query, (group_id,))
        return jsonify({"success": True, "members": members}), 200
    except Exception as e:
        current_app.logger.error(f"Error in list_group_members: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500
