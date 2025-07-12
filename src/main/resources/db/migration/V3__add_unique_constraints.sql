-- Add unique constraints for username field
-- Email unique constraint should already exist from User entity annotation

-- Add unique constraint to username field
ALTER TABLE users ADD CONSTRAINT users_username_unique UNIQUE (username);
