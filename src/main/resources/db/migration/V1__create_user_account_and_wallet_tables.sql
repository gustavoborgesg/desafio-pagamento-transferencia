CREATE TABLE IF NOT EXISTS wallet (
    id BIGSERIAL PRIMARY KEY,
    balance NUMERIC(19,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS user_accounts (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    wallet_id BIGINT UNIQUE,
    CONSTRAINT fk_wallet
        FOREIGN KEY (wallet_id)
        REFERENCES wallet(id)
);