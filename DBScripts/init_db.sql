-- ============================
-- DROP TABLES IF THEY EXIST
-- ============================
DROP TABLE IF EXISTS reading_list CASCADE;
DROP TABLE IF EXISTS reading_group_members CASCADE;
DROP TABLE IF EXISTS reading_groups CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS friend_requests CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================
-- CREATE TABLES
-- ============================
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
    external_id     TEXT NOT NULL,       -- e.g. Google Books ID
    suggested_by    INT,                 -- Who recommended it
    status          TEXT NOT NULL DEFAULT 'pending',  
    recommended_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    FOREIGN KEY (group_id) REFERENCES reading_groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (suggested_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ============================
-- SAMPLE DATA
-- ============================
INSERT INTO users (username, password) VALUES
('alice', 'password123'),
('bob', 'bobpass'),
('charlie', 'charliepass'),
('dave', 'davepass'),
('eve', 'evepass'),
('frank', 'frankpass'),
('grace', 'gracepass'),
('heidi', 'heidipass'),
('ivan', 'ivanpass'),
('judy', 'judypass');

-- FRIENDSHIPS
INSERT INTO friendships (user_id, friend_id) VALUES
(1, 2),
(2, 1),
(2, 3),
(1, 3),
(4, 5),
(5, 4),
(7, 8),
(8, 7),
(1, 10),
(10, 1);

-- FRIEND REQUESTS
INSERT INTO friend_requests (sender_id, receiver_id, status) VALUES
(1, 4, 'pending'),   
(1, 5, 'pending'),   
(6, 1, 'pending'),   
(2, 6, 'pending'),   
(9, 1, 'pending'),   
(2, 7, 'denied'),    
(8, 6, 'pending'),   
(9, 10, 'denied'),   
(3, 6, 'approved'),  
(8, 9, 'pending');   

-- READING GROUPS
INSERT INTO reading_groups (group_name, created_by) VALUES
('Sci-Fi Fans', 1),      
('Mystery Lovers', 2),   
('Fantasy Realm', 3),    
('Non-Fiction Buffs', 4);

-- READING GROUP MEMBERS
INSERT INTO reading_group_members (group_id, user_id, role) VALUES
(1, 1, 'admin'),  
(1, 2, 'member'), 
(1, 3, 'member'), 
(1, 7, 'member'),

(2, 2, 'admin'),  
(2, 1, 'member'), 
(2, 5, 'member'), 
(2, 9, 'member'),

(3, 3, 'admin'),  
(3, 4, 'member'), 
(3, 8, 'member'), 
(3, 10, 'member'),

(4, 4, 'admin'),  
(4, 6, 'member'), 
(4, 2, 'member');

-- READING LIST (using external_id)
INSERT INTO reading_list (group_id, external_id, suggested_by, status) VALUES
-- Group 1 (Sci-Fi Fans) suggestions:
(1, 'GB_DUNE_ID', 1, 'approved'), 
(1, 'GB_NEUROMANCER_ID', 2, 'approved'),
(1, 'OL_1984_ID', 3, 'pending'),

-- Group 2 (Mystery Lovers):
(2, 'OL_SHERLOCK_ID', 2, 'approved'),
(2, 'GB_MARTIAN_ID', 5, 'pending'),

-- Group 3 (Fantasy Realm):
(3, 'OL_HOBBIT_ID', 8, 'approved'),
(3, 'GB_NAME_OF_WIND_ID', 3, 'pending'),

-- Group 4 (Non-Fiction Buffs):
(4, 'GB_EDUCATED_ID', 4, 'approved'),
(4, 'GB_SOME_NF_BOOK', 6, 'pending');
