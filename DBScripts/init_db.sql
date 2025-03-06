-- ============================
-- DROP TABLES IF THEY EXIST
-- ============================
DROP TABLE IF EXISTS reading_list CASCADE;
DROP TABLE IF EXISTS reading_group_members CASCADE;
DROP TABLE IF EXISTS reading_groups CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friend_requests CASCADE;

-- ============================
-- CREATE TABLES
-- ============================
CREATE TABLE users (
    user_id     SERIAL PRIMARY KEY,
    username    TEXT NOT NULL UNIQUE,
    password    TEXT NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS friend_requests (
    id          SERIAL PRIMARY KEY,
    sender_id   INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    status      VARCHAR(10) NOT NULL DEFAULT 'pending',
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS friendships (
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

CREATE TABLE books (
    book_id     SERIAL PRIMARY KEY,
    external_id TEXT,                 -- For lookups in Google
    title       TEXT NOT NULL,
    author      TEXT,
    preview_url TEXT,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE reading_list (
    group_id      INT NOT NULL,
    book_id       INT NOT NULL,
    suggested_by  INT,                              
    status        TEXT NOT NULL DEFAULT 'pending',  
    recommended_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    PRIMARY KEY (group_id, book_id),
    FOREIGN KEY (group_id) REFERENCES reading_groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (suggested_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- ============================
-- USERS
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

-- ============================
-- FRIENDSHIPS
-- ============================
-- For testing, we insert friendships in both directions.
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

-- ============================
-- FRIEND REQUESTS
-- ============================
INSERT INTO friend_requests (sender_id, receiver_id, status) VALUES
(1, 4, 'pending'),   -- alice -> dave
(1, 5, 'pending'),   -- alice -> eve
(6, 1, 'pending'),   -- frank -> alice
(2, 6, 'pending'),   -- bob -> frank
(9, 1, 'pending'),   -- ivan -> alice
(2, 7, 'denied'),    -- bob -> grace
(8, 6, 'pending'),   -- heidi -> frank
(9, 10, 'denied'),   -- ivan -> judy
(3, 6, 'approved'),  -- charlie -> frank
(8, 9, 'pending');   -- heidi -> ivan

-- ============================
-- READING GROUPS
-- ============================
INSERT INTO reading_groups (group_name, created_by) VALUES
('Sci-Fi Fans', 1),      
('Mystery Lovers', 2),   
('Fantasy Realm', 3),    
('Non-Fiction Buffs', 4);

-- ============================
-- READING GROUP MEMBERS
-- ============================
-- Role can be 'admin' or 'member'. 
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

-- ============================
-- BOOKS
-- ============================
-- Insert both Google Books (GB_) and Open Library (OL_) references
INSERT INTO books (external_id, title, author, preview_url) VALUES
('GB_ABC123', 'Dune', 'Frank Herbert', 'https://books.google.com/dune'),
('GB_DEF456', 'Neuromancer', 'William Gibson', 'https://books.google.com/neuromancer'),
('OL_789XYZ', 'The Hobbit', 'J.R.R. Tolkien', 'https://openlibrary.org/hobbit'),
('GB_GHI789', '1984', 'George Orwell', 'https://books.google.com/1984'),
('OL_111AAA', 'Sherlock Holmes', 'Arthur Conan Doyle', 'https://openlibrary.org/sherlock'),
('GB_222BBB', 'The Martian', 'Andy Weir', 'https://books.google.com/martian'),
('OL_333CCC', 'Educated', 'Tara Westover', 'https://openlibrary.org/educated'),
('GB_444DDD', 'The Name of the Wind', 'Patrick Rothfuss', 'https://books.google.com/nameofthewind');

-- ============================
-- READING LIST
-- ============================
-- Ties a group to a book, who suggested it, and a status ('pending' or 'approved').
INSERT INTO reading_list (group_id, book_id, suggested_by, status) VALUES
-- Group 1 (Sci-Fi Fans): 
(1, 1, 1, 'approved'),   
(1, 2, 2, 'approved'),   
(1, 4, 3, 'pending'),    

-- Group 2 (Mystery Lovers):
(2, 5, 2, 'approved'),   
(2, 3, 5, 'pending'),    

-- Group 3 (Fantasy Realm):
(3, 3, 8, 'approved'),   
(3, 8, 3, 'pending'),    

-- Group 4 (Non-Fiction Buffs):
(4, 7, 4, 'approved'),   
(4, 6, 6, 'pending');

