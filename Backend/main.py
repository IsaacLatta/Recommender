#!/usr/bin/python3

import psycopg2, os, requests

from flask import Flask, jsonify, request

app = Flask(__name__)

DB_HOST = os.environ.get("RDS_HOST")
DB_NAME = os.environ.get("RDS_NAME")
DB_USER = os.environ.get("RDS_USER")
DB_PASS = os.environ.get("RDS_PASS")
BOOK_API_KEY = os.environ.get("BOOK_API_KEY")
BOOK_API_URL = os.environ.get("BOOK_API_URL")

def connect_to_db():
    return psycopg2.connect(host=DB_HOST, database=DB_NAME, user=DB_USER, password=DB_PASS)

def parse_books(data):
    results = []
    items = data.get("items", [])  

    for item in items:
        volume_info = item.get("volumeInfo", {})
        title = volume_info.get("title", "No Title")
        authors = volume_info.get("authors", [])
        publisher = volume_info.get("publisher", "")
        published_date = volume_info.get("publishedDate", "")
        description = volume_info.get("description", "")
        preview_link = volume_info.get("previewLink", "")
        image_links = volume_info.get("imageLinks", {})
        thumbnail = image_links.get("thumbnail", "")

        isbn_13 = None
        for identifier in volume_info.get("industryIdentifiers", []):
            if identifier.get("type") == "ISBN_13":
                isbn_13 = identifier.get("identifier")
                break

        results.append({
            "title": title,
            "authors": authors,
            "publisher": publisher,
            "publishedDate": published_date,
            "description": description,
            "previewLink": preview_link,
            "thumbnail": thumbnail,
            "isbn_13": isbn_13
        })

    return results

@app.route('/search_book', methods=['GET'])
def search_book():
    try:
        query = request.args.get("q")
        if not query:
            return jsonify({"success": False, "error": "No query parameter provided"}), 400

        params = {"q": query, "key": BOOK_API_KEY}
        response = requests.get(BOOK_API_URL, params=params)

        if response.status_code != 200:
            return jsonify({"success": False, "error": "Error from Google Books API", "status": response.status_code}), 500

        return jsonify({
            "search_term": query,
            "results": parse_books(response.json()),
            "success": True
        }), 200

    except Exception as e:
        return jsonify({"success": False, "error": str(e)}), 500


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
            user_id = row[0]
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
