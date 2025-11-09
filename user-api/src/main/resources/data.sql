DELETE FROM users;
DELETE FROM roles;

INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_GUEST'),
('ROLE_VIEWER')
;

INSERT INTO users (handle, email, password, created_at) VALUES
("Bruno", "bruno@email.com", "s123", CURRENT_TIMESTAMP),
("Adriano", "adriano@email.com", "senha123", CURRENT_TIMESTAMP)

INSERT INTO island (name) VALUES 
('Ilha-Alpha');

-- Workstation 1: LIVRE
INSERT INTO workstation (identifier, island_id, user_id) VALUES 
('WS-A1', 1, NULL);

-- Workstation 2: LIVRE
INSERT INTO workstation (identifier, island_id, user_id) VALUES 
('WS-A2', 1, NULL);

-- Workstation 3: OCUPADA
INSERT INTO workstation (identifier, island_id, user_id) VALUES 
('WS-A3', 1, 2);