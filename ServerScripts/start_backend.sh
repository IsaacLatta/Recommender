

if [ -z "$RDS_HOST" ] || [ -z "$RDS_USER" ] || [ -z "$RDS_NAME" ] || [ -z "$RDS_PASS" ]; then
  echo "[-] Error: Database environment variables (RDS_HOST, RDS_USER, RDS_NAME, RDS_PASS) not set."
  exit 1
fi

echo "[*] Re-initializing database: $RDS_NAME on host $RDS_HOST..."
PGPASSWORD="$RDS_PASS" psql -h "$RDS_HOST" -U "$RDS_USER" -d "$RDS_NAME" -f seed_db.sql
if [ $? -ne 0 ]; then
  echo "[-] Error applying seed_db.sql"
  exit 1
fi

echo "[+] Database seeded"
PGPASSWORD="$RDS_PASS" psql -h "$RDS_HOST" -U "$RDS_USER" -d "$RDS_NAME" \
  -c "\dt"

# =========================
# Start the Flask backend in the background
# =========================

echo "[*] Starting Flask server ..."
echo "[!] Press Ctrl+C to stop."

# Example: If your Flask entry point is `app.py` or `main.py`, etc.
# Adjust command for your environment:
FLASK_APP=../Backend/run.py FLASK_ENV=development flask run --host=0.0.0.0 --port=5022 &
SERVER_PID=$!
sleep 3  

echo "[+] Flask server started with PID $SERVER_PID"

cleanup() {
  kill $SERVER_PID
  wait $SERVER_PID 2>/dev/null
  echo "[+] Server PID $SERVER_PID stopped."
  exit 0
}

trap cleanup SIGINT SIGTERM

while true; do 
  sleep 1
done