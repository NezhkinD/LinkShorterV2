CREATE TABLE IF NOT EXISTS links
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    original           TEXT NOT NULL,
    short              VARCHAR(30)  NOT NULL UNIQUE,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expired_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user               VARCHAR(36)  NOT NULL,
    transition_limit   int       DEFAULT 0,
    transition_current int       DEFAULT 0
);
