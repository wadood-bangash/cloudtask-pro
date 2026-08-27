-- Insert sample users
INSERT INTO users (username, email, password_hash, full_name, role) VALUES
('admin', 'admin@cloudtask.com', '$2a$10$YourEncryptedPasswordHere', 'Admin User', 'ADMIN'),
('john_doe', 'john@example.com', '$2a$10$YourEncryptedPasswordHere', 'John Doe', 'USER'),
('jane_smith', 'jane@example.com', '$2a$10$YourEncryptedPasswordHere', 'Jane Smith', 'USER');

-- Insert sample tasks
INSERT INTO tasks (title, description, status, priority, due_date, user_id, assigned_to) VALUES
('Setup CloudTask Pro', 'Initialize project with Spring Boot and Docker', 'COMPLETED', 'HIGH', CURRENT_TIMESTAMP + INTERVAL '7 days', 1, 1),
('Design Database Schema', 'Create users and tasks tables with relationships', 'IN_PROGRESS', 'HIGH', CURRENT_TIMESTAMP + INTERVAL '14 days', 2, 2),
('Implement JWT Authentication', 'Add Spring Security and JWT token generation', 'PENDING', 'MEDIUM', CURRENT_TIMESTAMP + INTERVAL '21 days', 3, 3),
('Deploy to AWS EC2', 'Setup CI/CD pipeline and deploy to production', 'PENDING', 'HIGH', CURRENT_TIMESTAMP + INTERVAL '30 days', 1, 2);
