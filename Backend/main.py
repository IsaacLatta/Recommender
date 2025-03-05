#!/usr/bin/python3
import psycopg2, os, requests, jwt, datetime
from flask import Flask, jsonify, request

app = Flask(__name__)

DB_HOST = os.environ.get("RDS_HOST")
DB_NAME = os.environ.get("RDS_NAME")
DB_USER = os.environ.get("RDS_USER")
DB_PASS = os.environ.get("RDS_PASS")
BOOK_API_KEY = os.environ.get("BOOK_API_KEY")
BOOK_API_URL = os.environ.get("BOOK_API_URL")

JWT_SECRET = os.environ.get("JWT_SECRET", "default_secret")  
JWT_ALGORITHM = "HS256"
JWT_EXP_DELTA_DAYS = 7  

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
    print("Results Size: ", len(results))
    return results


def authenticate(auth_header):
    return True # For testing purposes
    if auth_header is None or not auth_header.startswith("Bearer "):
            return jsonify({"success": False, "error": "Missing or invalid token"}), 401
    token = auth_header[len("Bearer "):]
    try:
        payload = jwt.decode(token, JWT_SECRET, algorithms=[JWT_ALGORITHM])
    except jwt.ExpiredSignatureError:
        return False
    except jwt.InvalidTokenError:
        return False
    return True

@app.route('/search_book', methods=['GET'])
def search_book():
    if not authenticate(request.headers.get("Authorization")):
        return jsonify({"success": False, "error": "Invalid token"}), 401
    
    query = request.args.get("q")
    print("Received query:", query)
    if not query:
        return jsonify({"success": False, "error": "No query parameter provided"}), 400

    params = {"q": query, "key": BOOK_API_KEY}
    response = requests.get(BOOK_API_URL, params=params)

    if response.status_code != 200:
        return jsonify({
            "success": False,
            "error": "Error from Google Books API",
            "status": response.status_code
        }), 500

    book_list = parse_books(response.json())
    return jsonify({
        "totalItems": len(book_list),
        "items": book_list,
        "success": True
    }), 200

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
            token = jwt.encode({
                'user_id': user_id,
                'exp': datetime.datetime.utcnow() + datetime.timedelta(days=JWT_EXP_DELTA_DAYS)
            }, JWT_SECRET, algorithm=JWT_ALGORITHM)
            return jsonify({
                "message": "Login successful",
                "username": username,
                "user_id": user_id,
                "token": token,
                "success": True
            }), 200
        else:
            return jsonify({
                "message": "Login failed",
                "username": username,
                "success": False
            }), 401
    except Exception as e:
        print("Login error:", e)
        return jsonify({"message": "Server error", "success": False}), 500
    finally:
        if cxn:
            cxn.close()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5022)
