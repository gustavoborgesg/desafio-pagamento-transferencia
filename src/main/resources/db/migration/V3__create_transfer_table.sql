CREATE TABLE IF NOT EXISTS transfer (
    id BIGSERIAL PRIMARY KEY,
    payer_id BIGINT NOT NULL,
    payee_id BIGINT NOT NULL,
    amount NUMERIC(19,2) NOT NULL CHECK (amount > 0),
    timestamp TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_transfer_payer
        FOREIGN KEY (payer_id)
        REFERENCES user_accounts(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_transfer_payee
        FOREIGN KEY (payee_id)
        REFERENCES user_accounts(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE INDEX idx_transfer_payer_id ON transfer (payer_id);
CREATE INDEX idx_transfer_payee_id ON transfer (payee_id);
