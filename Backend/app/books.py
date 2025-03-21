import requests
from flask import Blueprint, jsonify, request, current_app
from .db import run_query
from .friends import get_current_user

books_bp = Blueprint('books', __name__)

def parse_books(data):
    results = []
    items = data.get("items", [])
    for item in items:
        volume_info = item.get("volumeInfo", {})
        results.append({
            "external_id": item.get("id"),
            # "external_id": volume_info.get("id"),
            "title": volume_info.get("title", "No Title"),
            "authors": volume_info.get("authors", []),
            "publisher": volume_info.get("publisher", ""),
            "publishedDate": volume_info.get("publishedDate", ""),
            "description": volume_info.get("description", ""),
            "previewLink": volume_info.get("previewLink", ""),
            "thumbnail": volume_info.get("imageLinks", {}).get("thumbnail", ""),
            "isbn_13": next((
                identifier.get("identifier")
                for identifier in volume_info.get("industryIdentifiers", [])
                if identifier.get("type") == "ISBN_13"
            ), None)
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
2
@books_bp.route('/book', methods=['POST'])  
def manage_book_actions():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    data = request.get_json()
    external_id = data.get("external_id")
    action = data.get("action", "").lower()

    save = ("save" in action)
    rate = ("rate" in action)
    if save:
        print("Save found")

    if external_id:
        print("ExternalId found")

    if not (save or rate) or not external_id:
        return jsonify({"success": False, "error": "Invalid arguments"}), 400

    try:
        existing = run_query(
            "SELECT id, saved, rating FROM user_books WHERE user_id = %s AND external_id = %s",
            (user_id, external_id)
        )

        if existing:
            record_id = existing[0]["id"]
            current_saved = existing[0]["saved"]
            current_rating = existing[0]["rating"]

            if save:
                current_saved = True
            if rate:
                new_rating = data.get("rating")
                if not isinstance(new_rating, int):
                    return jsonify({"success": False, "error": "Invalid rating; must be int"}), 400
                current_rating = new_rating

            run_query(
                """
                UPDATE user_books
                SET saved = %s,
                    rating = %s,
                    updated_at = NOW()
                WHERE id = %s
                """,
                (current_saved, current_rating, record_id),
                fetch=False,
                commit=True
            )
        else:
            new_rating = None
            if rate:
                new_rating = data.get("rating")
                if not isinstance(new_rating, int):
                    return jsonify({"success": False, "error": "Invalid rating; must be int"}), 400

            run_query(
                """
                INSERT INTO user_books (user_id, external_id, saved, rating)
                VALUES (%s, %s, %s, %s)
                """,
                (user_id, external_id, save, new_rating),
                fetch=False,
                commit=True
            )

        return jsonify({"success": True, "message": "Book updated successfully"}), 200

    except Exception as e:
        current_app.logger.error(f"Error in /book: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500

def fetch_from_google(rows, extra_fields=None):
    if not extra_fields:
        extra_fields = []

    results = []
    for row in rows:
        volume_id = row.get("external_id")
        if not volume_id:
            continue

        extra_data = {}
        for field in extra_fields:
            extra_data[field] = row.get(field)

        url = f"{current_app.config['BOOK_API_URL']}/{volume_id}"
        params = {"key": current_app.config["BOOK_API_KEY"]}
        g_response = requests.get(url, params=params)
        if g_response.status_code != 200:
            current_app.logger.warning(f"Google API error for volume_id={volume_id}: {g_response.status_code}")
            results.append({
                "external_id": volume_id,
                "title": None,
                "authors": [],
                "thumbnail": None,
                "description": None,
                "previewLink": None,
                **extra_data
            })
            continue

        data = g_response.json()
        volume_info = data.get("volumeInfo", {})
        results.append({
            "external_id": volume_id,
            "title": volume_info.get("title", "No Title"),
            "authors": volume_info.get("authors", []),
            "description": volume_info.get("description", ""),
            "previewLink": volume_info.get("previewLink", ""),
            "thumbnail": volume_info.get("imageLinks", {}).get("thumbnail", ""),
            **extra_data
        })
    return results


@books_bp.route('/book', methods=['GET'])
def list_books():
    user_id = get_current_user()
    if not user_id:
        return jsonify({"success": False, "error": "Invalid token"}), 401

    try:
        rows = run_query(
            "SELECT external_id, rating FROM user_books WHERE user_id = %s AND saved = TRUE",
            (user_id,)
        )
        if not rows:
            return jsonify({"success": True, "books": []}), 200

        results = fetch_from_google(rows, extra_fields=["rating"])
        return jsonify({"success": True, "items": results}), 200

    except Exception as e:
        current_app.logger.error(f"Error in list_books: {e}")
        return jsonify({"success": False, "error": "Server error"}), 500