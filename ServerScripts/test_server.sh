#!/usr/bin/env bash

# =========================
# Pre-check environment
# =========================
VERBOSE=0

if [ -z "$RDS_HOST" ] || [ -z "$RDS_USER" ] || [ -z "$RDS_NAME" ] || [ -z "$RDS_PASS" ]; then
  echo "[-] Error: Database environment variables (RDS_HOST, RDS_USER, RDS_NAME, RDS_PASS) not set."
  exit 1
fi

# =========================
# 1. Re-initialize the database
# =========================

echo "[*] Starting test script ..."

if [[ "$1" == "--verbose" || "$1" == "-v" ]]; then
    echo "[+] Verbose mode enabled."
    VERBOSE=1
else
    echo "[!] Run with -v or --verbose to see database state before and after tests."
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
# 2. Start the Flask backend in the background
# =========================

echo "[*] Starting Flask server ..."
# Example: If your Flask entry point is `app.py` or `main.py`, etc.
# Adjust command for your environment:
FLASK_APP=../Backend/run.py FLASK_ENV=development flask run --host=0.0.0.0 --port=5022 &
SERVER_PID=$!
sleep 3  # Give the server a few seconds to start

echo "[+] Flask server started with PID $SERVER_PID"

# =========================
# 3. Run cURL tests
# =========================
echo "[*] Testing /login endpoint ..."
curl -s -X POST http://localhost:5022/login \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "password": "password123"}' | tee /tmp/login_response.json
echo ""

# Extract token from the login response.
TOKEN=$(cat /tmp/login_response.json | jq -r '.token')
echo "[+] Got token: $TOKEN"
echo "[*] Signed in as alice [user_id = 1]"

echo "[*] Beginning tests for group operations."

# 3a) Test creating a reading group with alice's token
echo "[*] Testing create_group ..."
curl -s -X POST http://localhost:5022/reading/group/create \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"group_name":"TestingGroup"}' | tee /tmp/create_group.json
echo ""

# 3b) Search reading groups
echo "[*] Testing search_groups ..."
curl -s -X GET 'http://localhost:5022/reading/group/search?q=Fans' \
  -H "Authorization: Bearer $TOKEN" | tee /tmp/search_groups.json
echo ""

# 3c) Join group
echo "[*] Testing join_group ..."
curl -s -X POST http://localhost:5022/reading/group/join \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"group_id":2}' | tee /tmp/join_group.json
echo ""

# 3d) Recommend a book
echo "[*] Testing recommend_book_to_group..."
curl -s -X POST http://localhost:5022/reading/group/recommend \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"group_id":2, "external_id":"GB_TEST_BOOK_ID"}' | tee /tmp/recommend_book.json
echo ""

# 3e) Promote a user (assuming user 3 is in group 2)
echo "[*] Testing promote_group_member..."
curl -s -X POST http://localhost:5022/reading/group/promote \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"group_id":1, "member_id":2}' | tee /tmp/promote_member.json
echo ""

# =========================
# 4. Check DB State
# =========================

# PGPASSWORD="$RDS_PASS" psql -h "$RDS_HOST" -U "$RDS_USER" -d "$RDS_NAME" \
#   -c "SELECT group_id, external_id, status FROM reading_list ORDER BY id;"

if [[ "$VERBOSE" -eq 1 ]]; then
    echo "[*] Verifying DB changes ..."
    PGPASSWORD="$RDS_PASS" psql -h "$RDS_HOST" -U "$RDS_USER" -d "$RDS_NAME" -t -c "
    SELECT 'SELECT * FROM ' || tablename || ' LIMIT 10;' 
    FROM pg_tables WHERE schemaname = 'public';" | PGPASSWORD="$RDS_PASS" psql -h "$RDS_HOST" -U "$RDS_USER" -d "$RDS_NAME"
fi

echo "[*] Group operation tests complete, beginning friend operation tests ..."
echo "[*] Friend operation tests begin ..."

# 4a) List all friends for alice
echo "[*] Testing /friend/list ..."
curl -s -X GET http://localhost:5022/friend/list \
  -H "Authorization: Bearer $TOKEN" | tee /tmp/friend_list.json
echo ""

# 4b) List all friend requests (pending) for alice
echo "[*] Testing /friend/requests ..."
curl -s -X GET http://localhost:5022/friend/requests \
  -H "Authorization: Bearer $TOKEN" | tee /tmp/friend_requests.json
echo ""

# 4c) Approve a pending friend request
echo "[*] Approving friend request from heidi (user_id=5) ..."
curl -s -X POST http://localhost:5022/friend/request \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"action":"approve", "sender_id":5}' | tee /tmp/friend_approve.json
echo ""

# 4d) Deny a pending friend request
echo "[*] Denying friend request from dave (user_id=4) ..."
curl -s -X POST http://localhost:5022/friend/request \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"action":"deny", "sender_id":4}' | tee /tmp/friend_deny.json
echo ""

# 4e) Search for new friends
echo "[*] Testing /friend/search ..."
# Searching for 'char' should find 'charlie'.
curl -s -X GET 'http://localhost:5022/friend/search?q=char' \
  -H "Authorization: Bearer $TOKEN" | tee /tmp/friend_search.json
echo ""

# 4f) Remove an existing friend
echo "[*] Removing friend bob (user_id=2) ..."
curl -s -X POST http://localhost:5022/friend/remove \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"friend_id":2}' | tee /tmp/friend_remove.json
echo ""

# 4g) List updated friends again
echo "[*] Listing updated friends after removal..."
curl -s -X GET http://localhost:5022/friend/list \
  -H "Authorization: Bearer $TOKEN" | tee /tmp/friend_list_after.json
echo ""

if [[ "$VERBOSE" -eq 1 ]]; then
    echo "[*] Verifying DB changes ..."
    PGPASSWORD="$RDS_PASS" psql -h "$RDS_HOST" -U "$RDS_USER" -d "$RDS_NAME" -t -c "
    SELECT 'SELECT * FROM ' || tablename || ' LIMIT 10;' 
    FROM pg_tables WHERE schemaname = 'public';" | PGPASSWORD="$RDS_PASS" psql -h "$RDS_HOST" -U "$RDS_USER" -d "$RDS_NAME"
fi

echo "[*] Friend operation tests complete."

# =========================
# 5. Stop the Flask server
# =========================
echo "[*] Stopping Flask server PID $SERVER_PID ..."
kill $SERVER_PID
wait $SERVER_PID 2>/dev/null
echo "[+] Server stopped."
echo "[*] All tests complete."
