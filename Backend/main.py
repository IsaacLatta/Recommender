#!/usr/bin/python3

from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route('/')
def home():
    return jsonify({"message": "API is running!"})

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")

    return jsonify({"message": "Login endpoint reached", "username": username, "password": password, "success": True}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5022)
