CREATE TABLE IF NOT EXISTS admin (
    id BIGINT PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    CONSTRAINT fk_admin_user
        FOREIGN KEY (id)
        REFERENCES user_accounts(id)
);

CREATE TABLE IF NOT EXISTS client (
    id BIGINT PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    CONSTRAINT fk_client_user
        FOREIGN KEY (id)
        REFERENCES user_accounts(id)
);

CREATE TABLE IF NOT EXISTS merchant (
    id BIGINT PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    CONSTRAINT fk_merchant_user
        FOREIGN KEY (id)
        REFERENCES user_accounts(id)
);
