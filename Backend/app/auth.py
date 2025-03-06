import datetime, jwt
from flask import Blueprint, jsonify, request, current_app
from .db import run_query

auth_bp = Blueprint('auth', __name__)

@auth_bp.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")
    try:
        results = run_query("SELECT user_id FROM users WHERE username = %s AND password = %s", (username, password))
        if results:
            user_id = results[0]["user_id"]
            token = jwt.encode({
                'user_id': user_id,
                'exp': datetime.datetime.utcnow() + datetime.timedelta(days=current_app.config["JWT_EXP_DELTA_DAYS"])
            }, current_app.config["JWT_SECRET"], algorithm=current_app.config["JWT_ALGORITHM"])
            return jsonify({
                "message": "Login successful",
                "username": username,
                "user_id": user_id,
                "token": token,
                "success": True
            }), 200
        else:
            return jsonify({"message": "Login failed", "username": username, "success": False}), 401
    except Exception as e:
        current_app.logger.error(f"Login error: {e}")
        return jsonify({"message": "Server error", "success": False}), 500
