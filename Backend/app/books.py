import requests
from flask import Blueprint, jsonify, request, current_app
from .db import run_query

books_bp = Blueprint('books', __name__)

def parse_books(data):
    results = []
    items = data.get("items", [])
    for item in items:
        volume_info = item.get("volumeInfo", {})
        results.append({
            "title": volume_info.get("title", "No Title"),
            "authors": volume_info.get("authors", []),
            "publisher": volume_info.get("publisher", ""),
            "publishedDate": volume_info.get("publishedDate", ""),
            "description": volume_info.get("description", ""),
            "previewLink": volume_info.get("previewLink", ""),
            "thumbnail": volume_info.get("imageLinks", {}).get("thumbnail", ""),
            "isbn_13": next((identifier.get("identifier") for identifier in volume_info.get("industryIdentifiers", []) if identifier.get("type") == "ISBN_13"), None)
        })
    return results

@books_bp.route('/search_book', methods=['GET'])
def search_book():
    search_term = request.args.get("q")
    if not search_term:
        return jsonify({"success": False, "error": "No query parameter provided"}), 400

    params = {"q": search_term, "key": current_app.config["BOOK_API_KEY"]}
    response = requests.get(current_app.config["BOOK_API_URL"], params=params)
    if response.status_code != 200:
        return jsonify({
            "success": False,
            "error": "Error from Books API",
            "status": response.status_code
        }), 500

    book_list = parse_books(response.json())
    return jsonify({
        "totalItems": len(book_list),
        "items": book_list,
        "success": True
    }), 200
