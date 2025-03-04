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
-- Each row indicates a friendship between two users.
-- For simplicity, only one-directional entries.
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
-- READING GROUPS
-- ============================
INSERT INTO reading_groups (group_name, created_by) VALUES
('Sci-Fi Fans', 1),      
('Mystery Lovers', 2),   
('Fantasy Realm', 3),    
('Non-Fiction Buffs', 4) 
;

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
-- Added both google books, and open lib since we have not picked a book api
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
-- 6) READING LIST
-- ============================
-- Each row ties a group to a book, with who suggested it, and a status ('pending' or 'approved').
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
