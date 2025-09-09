-- V1__Create_initial_tables.sql
-- Initial database schema creation for ElderBridge application

-- Create USER table
CREATE TABLE USER (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    code_number VARCHAR(255),
    expiration_code DATETIME
);

-- Create SOCIETY table
CREATE TABLE SOCIETY (
    id_society BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    location VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    creator_id BIGINT NOT NULL,
    description TEXT,
    FOREIGN KEY (creator_id) REFERENCES USER(user_id)
);

-- Create EVENT table
CREATE TABLE EVENT (
    event_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    event_date DATETIME NOT NULL,
    description TEXT,
    event_location VARCHAR(255)
);

-- Create MEMBERSHIP table
CREATE TABLE MEMBERSHIP (
    id_member_ship BIGINT AUTO_INCREMENT PRIMARY KEY,
    society_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    FOREIGN KEY (society_id) REFERENCES SOCIETY(id_society),
    FOREIGN KEY (member_id) REFERENCES USER(user_id),
    UNIQUE KEY unique_membership (society_id, member_id)
);

-- Create EVENTUSER table
CREATE TABLE EVENTUSER (
    id_event_user BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES EVENT(event_id),
    FOREIGN KEY (member_id) REFERENCES USER(user_id),
    UNIQUE KEY unique_event_user (event_id, member_id)
);

-- Create indexes for better performance
CREATE INDEX idx_user_email ON USER(email);
CREATE INDEX idx_society_name ON SOCIETY(name);
CREATE INDEX idx_society_creator ON SOCIETY(creator_id);
CREATE INDEX idx_event_name ON EVENT(name);
CREATE INDEX idx_event_date ON EVENT(event_date);
CREATE INDEX idx_membership_society ON MEMBERSHIP(society_id);
CREATE INDEX idx_membership_member ON MEMBERSHIP(member_id);
CREATE INDEX idx_eventuser_event ON EVENTUSER(event_id);
CREATE INDEX idx_eventuser_member ON EVENTUSER(member_id); 