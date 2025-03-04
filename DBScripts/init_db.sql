DROP TABLE IF EXISTS reading_list CASCADE;
DROP TABLE IF EXISTS reading_group_members CASCADE;
DROP TABLE IF EXISTS reading_groups CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    user_id     SERIAL PRIMARY KEY,
    username    TEXT NOT NULL UNIQUE,
    password    TEXT NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE friendships (
    user_id     INT NOT NULL,
    friend_id   INT NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE
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
    external_id TEXT,                 -- For lookups in Google/Open Library
    title       TEXT NOT NULL,
    author      TEXT,
    preview_url TEXT,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE reading_list (
    group_id      INT NOT NULL,
    book_id       INT NOT NULL,
    suggested_by  INT,                            -- user who suggested this book
    status        TEXT NOT NULL DEFAULT 'pending', -- or 'approved'
    recommended_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    PRIMARY KEY (group_id, book_id),
    FOREIGN KEY (group_id) REFERENCES reading_groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (suggested_by) REFERENCES users(user_id) ON DELETE SET NULL
);

