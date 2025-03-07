from flask import Flask
from .config import Config
from .db import init_db
from .auth import auth_bp
from .books import books_bp
from .friends import friends_bp
from .reading import reading_bp

def create_app():
    app = Flask(__name__)
    app.config.from_object(Config)

    init_db()

    app.register_blueprint(auth_bp)
    app.register_blueprint(books_bp)
    app.register_blueprint(friends_bp)
    app.register_blueprint(reading_bp)


    return app
