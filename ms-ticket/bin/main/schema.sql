DROP TABLE IF EXISTS tickets_observers;
DROP TABLE IF EXISTS tickets;

CREATE TABLE IF NOT EXISTS tickets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    creator_email VARCHAR(255) NOT NULL,
    assignee_email VARCHAR(255) NOT NULL,
    object VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL,
    details VARCHAR(255) NOT NULL,
    locality VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tickets_observers (
    ticket_id INTEGER NOT NULL,
    observer_email VARCHAR(255) NOT NULL,
    PRIMARY KEY (ticket_id, observer_email),
    FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);