-- Create database
CREATE DATABASE forumDB;

-- Use the created database
USE forumDB;

-- Create 'users' table for storing user details
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    role VARCHAR(20), -- 'Student' or 'Professor'
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255)
);

-- Create 'questions' table for storing questions posted by students
CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    question TEXT,
    FOREIGN KEY (username) REFERENCES users(username)
);

-- Create 'answers' table for storing answers given by professors
CREATE TABLE answers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT,
    user_id INT,
    answer TEXT,
    upvotes INT DEFAULT 0,
    downvotes INT DEFAULT 0,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);


-- Create 'votes' table to track upvotes and downvotes for answers
CREATE TABLE votes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,  -- The user who is voting
    answer_id INT,  -- The answer that is being voted on
    vote_type ENUM('upvote', 'downvote'),  -- The type of vote (up or down)
    UNIQUE(user_id, answer_id)  -- Ensure a user can only vote once per answer
);

