CREATE TABLE IF NOT EXISTS transfer (
    id BIGSERIAL PRIMARY KEY,
    payer_id BIGINT NOT NULL,
    payee_id BIGINT NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_transfer_payer
        FOREIGN KEY (payer_id)
        REFERENCES user_accounts(id),
    CONSTRAINT fk_transfer_payee
        FOREIGN KEY (payee_id)
        REFERENCES user_accounts(id)
);
