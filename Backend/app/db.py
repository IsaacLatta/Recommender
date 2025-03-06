import psycopg2
from flask import current_app

def connect_to_db():
    return psycopg2.connect(
        host=current_app.config["DB_HOST"],
        database=current_app.config["DB_NAME"],
        user=current_app.config["DB_USER"],
        password=current_app.config["DB_PASS"]
    )

def run_query(query, params=(), fetch=True, commit=False):
    cxn = None
    try:
        cxn = connect_to_db()
        curs = cxn.cursor()
        curs.execute(query, params)
        if commit:
            cxn.commit()
        if fetch:
            columns = [desc[0] for desc in curs.description]
            rows = curs.fetchall()
            return [dict(zip(columns, row)) for row in rows]
        return []
    except Exception as e:
        current_app.logger.error(f"Database query error: {e}")
        raise
    finally:
        if cxn:
            cxn.close()

def init_db():
    pass
