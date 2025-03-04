#!/usr/bin/python3

import psycopg2, os

from flask import Flask, jsonify, request

app = Flask(__name__)

DB_HOST = os.environ.get("RDS_HOST")
DB_NAME = os.environ.get("RDS_NAME")
DB_USER = os.environ.get("RDS_USER")
DB_PASS = os.environ.get("RDS_PASS")

def connect_to_db():
    return psycopg2.connect(host=DB_HOST, database=DB_NAME, user=DB_USER, password=DB_PASS)

@app.route('/')
def home():
    return jsonify({"message": "API is running!"})

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")

    cxn = None
    try:
        cxn = connect_to_db()
        curs = cxn.cursor()
        curs.execute("SELECT user_id FROM users WHERE username = %s AND password = %s", (username, password))
        row = curs.fetchone()
        if row:
            user_id = row
            return jsonify({"message": "Login successful", "username": username, "password": password, "user_id": user_id, "success": True}), 200
        else:
            return jsonify({"message": "Login failed", "username": username, "password": password, "success": False}), 401
    except Exception as e:
        print("Login error:", e)
        return jsonify({"message": "Server error", "success": False}), 500
    finally:
        if cxn:
            cxn.close()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5022)
