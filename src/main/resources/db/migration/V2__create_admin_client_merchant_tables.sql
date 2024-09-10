CREATE TABLE IF NOT EXISTS admin (
    id BIGINT PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    CONSTRAINT fk_admin_user
        FOREIGN KEY (id)
        REFERENCES user_accounts(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS client (
    id BIGINT PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    CONSTRAINT fk_client_user
        FOREIGN KEY (id)
        REFERENCES user_accounts(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS merchant (
    id BIGINT PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    CONSTRAINT fk_merchant_user
        FOREIGN KEY (id)
        REFERENCES user_accounts(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE INDEX idx_admin_cpf ON admin (cpf);
CREATE INDEX idx_client_cpf ON client (cpf);
CREATE INDEX idx_merchant_cnpj ON merchant (cnpj);
