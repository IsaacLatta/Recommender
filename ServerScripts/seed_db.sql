-- ==================================
-- Drop existing tables (in correct order to avoid FK issues)
-- ==================================
DROP TABLE IF EXISTS reading_list CASCADE;
DROP TABLE IF EXISTS reading_group_members CASCADE;
DROP TABLE IF EXISTS reading_groups CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS friend_requests CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ==================================
-- Create tables
-- ==================================
CREATE TABLE users (
    user_id     SERIAL PRIMARY KEY,
    username    TEXT NOT NULL UNIQUE,
    password    TEXT NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE friend_requests (
    id          SERIAL PRIMARY KEY,
    sender_id   INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    status      VARCHAR(10) NOT NULL DEFAULT 'pending',
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);

CREATE TABLE friendships (
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER NOT NULL,
    friend_id   INTEGER NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (friend_id) REFERENCES users(user_id)
);

CREATE TABLE user_books (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    external_id TEXT NOT NULL,
    saved BOOLEAN NOT NULL DEFAULT FALSE,
    rating INT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE (user_id, external_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


CREATE TABLE reading_groups (
    group_id    SERIAL PRIMARY KEY,
    group_name  TEXT NOT NULL,
    created_by  INT NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE reading_group_members (
    group_id    INT NOT NULL,
    user_id     INT NOT NULL,
    role        TEXT NOT NULL DEFAULT 'member',  -- 'admin' or 'member'
    joined_at   TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    PRIMARY KEY (group_id, user_id),
    FOREIGN KEY (group_id) REFERENCES reading_groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE reading_list (
    id              SERIAL PRIMARY KEY,
    group_id        INT NOT NULL,
    external_id     TEXT NOT NULL,       
    suggested_by    INT,                 
    status          TEXT NOT NULL DEFAULT 'pending',  
    recommended_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    FOREIGN KEY (group_id) REFERENCES reading_groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (suggested_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ==================================
-- Insert sample data
-- ==================================

-- Users
INSERT INTO users (username, password) VALUES
('alice', 'password123'),
('bob', 'bobpass'),
('charlie', 'charliepass'),
('dave', 'davepass'),
('heidi', 'heidipass'),
('frank', 'frankpass');

-- Friendships (alice & bob, bob & charlie, etc.)
INSERT INTO friendships (user_id, friend_id) VALUES
(1, 2),
(2, 1),
(2, 3),
(3, 2);

-- Friend requests
INSERT INTO friend_requests (sender_id, receiver_id, status) VALUES
(5, 1, 'pending'),  -- heidi -> alice
(6, 1, 'approved'); -- frank -> alice (already approved)

-- Reading groups
INSERT INTO reading_groups (group_name, created_by) VALUES
('Sci-Fi Fans', 1),      
('Mystery Lovers', 2);   

-- Group members
INSERT INTO reading_group_members (group_id, user_id, role) VALUES
(1, 1, 'admin'),  -- alice is admin of "Sci-Fi Fans"
(1, 2, 'member'), -- bob is member
(2, 2, 'admin');  -- bob is admin of "Mystery Lovers"

-- Some existing reading_list data
INSERT INTO reading_list (group_id, external_id, suggested_by, status) VALUES
(1, 'GB_DUNE_ID', 1, 'approved'),
(1, 'GB_NEUROMANCER_ID', 2, 'pending');

INSERT INTO reading_list (group_id, external_id, suggested_by, status)
VALUES (1, 'GB_RECOMMEND_ID', 1, 'pending');
INSERT INTO reading_list (group_id, external_id, suggested_by, status)
VALUES (1, 'afCxg5sogvAC', 2, 'approve');
INSERT INTO reading_list (group_id, external_id, suggested_by, status)
VALUES (1, '7nDPUjKVDHcC', 1, 'deny');
INSERT INTO reading_list (group_id, external_id, suggested_by, status)
VALUES (1, '12D6DwAAQBAJ', 1, 'pending');

-- Friendships
INSERT INTO friendships (user_id, friend_id)
VALUES
(1, 2),
(2, 1),  -- alice & bob are mutual friends
(2, 3),
(3, 2);

INSERT INTO friend_requests (sender_id, receiver_id, status)
VALUES (4, 1, 'pending');

INSERT INTO user_books (user_id, external_id, saved, rating)
VALUES (1, 'GB_HITCHHIKERS_GUIDE', true, NULL);
INSERT INTO user_books (user_id, external_id, saved, rating)
VALUES (1, 'GB_SAMPLE_BOOK', TRUE, 4);
INSERT INTO user_books (user_id, external_id, saved, rating)
VALUES (1, 'dpy3CwAAQBAJ', TRUE, 3);
INSERT INTO user_books (user_id, external_id, saved, rating)
VALUES (1, 'tNClBwAAQBAJ', TRUE, 4);
INSERT INTO user_books (user_id, external_id, saved, rating)
VALUES (1, 'mJHhEAAAQBAJ', TRUE, 4);

