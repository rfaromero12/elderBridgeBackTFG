-- V2__Insert_sample_data.sql
-- Insert sample data for testing purposes

-- Insert sample users
INSERT INTO USER (name, surname, email, password, rol) VALUES
('Admin', 'User', 'admin@elderbridge.com', '$2a$10$dummy.hash.for.testing', 'ADMIN'),
('John', 'Doe', 'john.doe@example.com', '$2a$10$dummy.hash.for.testing', 'USER'),
('Jane', 'Smith', 'jane.smith@example.com', '$2a$10$dummy.hash.for.testing', 'USER'),
('Bob', 'Johnson', 'bob.johnson@example.com', '$2a$10$dummy.hash.for.testing', 'USER');

-- Insert sample societies
INSERT INTO SOCIETY (name, location, email, creator_id, description) VALUES
('Elder Care Association', 'Madrid, Spain', 'info@eldercare.org', 1, 'Association dedicated to improving elderly care services'),
('Senior Wellness Group', 'Barcelona, Spain', 'contact@seniorwellness.es', 2, 'Group focused on promoting healthy aging'),
('Golden Years Club', 'Valencia, Spain', 'hello@goldenyears.es', 3, 'Social club for seniors to connect and engage');

-- Insert sample events
INSERT INTO EVENT (name, event_date, description, event_location) VALUES
('Health Workshop', '2024-12-15 10:00:00', 'Workshop on healthy aging and nutrition', 'Community Center, Madrid'),
('Social Gathering', '2024-12-20 16:00:00', 'Monthly social gathering for members', 'Senior Center, Barcelona'),
('Technology Training', '2024-12-25 14:00:00', 'Learn to use smartphones and tablets', 'Library, Valencia');

-- Insert sample memberships
INSERT INTO MEMBERSHIP (society_id, member_id) VALUES
(1, 2), -- John Doe joins Elder Care Association
(1, 3), -- Jane Smith joins Elder Care Association
(2, 3), -- Jane Smith joins Senior Wellness Group
(2, 4), -- Bob Johnson joins Senior Wellness Group
(3, 2), -- John Doe joins Golden Years Club
(3, 4); -- Bob Johnson joins Golden Years Club

-- Insert sample event registrations
INSERT INTO EVENTUSER (event_id, member_id) VALUES
(1, 2), -- John Doe registers for Health Workshop
(1, 3), -- Jane Smith registers for Health Workshop
(2, 3), -- Jane Smith registers for Social Gathering
(2, 4), -- Bob Johnson registers for Social Gathering
(3, 2), -- John Doe registers for Technology Training
(3, 4); -- Bob Johnson registers for Technology Training 