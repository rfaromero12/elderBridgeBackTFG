-- V3__Example_future_migration.sql
-- This is an example file showing how to create future migrations
-- You can delete this file or use it as a template

-- Example: Adding new columns to existing tables
-- ALTER TABLE USER ADD COLUMN phone VARCHAR(20);
-- ALTER TABLE USER ADD COLUMN birth_date DATE;
-- ALTER TABLE USER ADD COLUMN address TEXT;

-- Example: Creating new indexes for performance
-- CREATE INDEX idx_user_phone ON USER(phone);
-- CREATE INDEX idx_user_birth_date ON USER(birth_date);

-- Example: Adding new constraints
-- ALTER TABLE USER ADD CONSTRAINT chk_user_age CHECK (birth_date <= CURRENT_DATE);

-- Example: Creating new tables
-- CREATE TABLE USER_PREFERENCES (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     user_id BIGINT NOT NULL,
--     theme VARCHAR(20) DEFAULT 'light',
--     notifications_enabled BOOLEAN DEFAULT true,
--     language VARCHAR(10) DEFAULT 'es',
--     FOREIGN KEY (user_id) REFERENCES USER(user_id)
-- );

-- Example: Modifying existing columns
-- ALTER TABLE USER MODIFY COLUMN rol ENUM('ADMIN', 'USER', 'MODERATOR') NOT NULL DEFAULT 'USER';

-- Example: Adding new foreign key relationships
-- ALTER TABLE EVENT ADD COLUMN organizer_id BIGINT;
-- ALTER TABLE EVENT ADD FOREIGN KEY (organizer_id) REFERENCES USER(user_id);

-- Note: This file is for demonstration purposes only
-- Delete it or replace with actual migration when needed 