import os

class Config:
    DB_HOST = os.environ.get("RDS_HOST")
    DB_NAME = os.environ.get("RDS_NAME")
    DB_USER = os.environ.get("RDS_USER")
    DB_PASS = os.environ.get("RDS_PASS")
    JWT_SECRET = os.environ.get("JWT_SECRET", "default_secret")
    JWT_ALGORITHM = "HS256"
    JWT_EXP_DELTA_DAYS = 7
    BOOK_API_KEY = os.environ.get("BOOK_API_KEY")
    BOOK_API_URL = os.environ.get("BOOK_API_URL")
