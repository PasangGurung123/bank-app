CREATE TABLE transfers (
    id BIGSERIAL PRIMARY KEY,
    amount DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    from_account_id BIGINT NOT NULL,
    to_account_id BIGINT NOT NULL,
    CONSTRAINT fk_from_account
        FOREIGN KEY(from_account_id)
        REFERENCES accounts(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_to_account
        FOREIGN KEY(to_account_id)
        REFERENCES accounts(id)
        ON DELETE CASCADE
);

-- Optional indexes for faster queries
CREATE INDEX idx_transfers_from_account_id ON transfers(from_account_id);
CREATE INDEX idx_transfers_to_account_id ON transfers(to_account_id);
