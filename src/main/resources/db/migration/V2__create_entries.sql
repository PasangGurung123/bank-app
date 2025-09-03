CREATE TABLE entries (
    id BIGSERIAL PRIMARY KEY,
    amount DECIMAL(19,2) NOT NULL,
    entry_type VARCHAR(50) NOT NULL, -- store enum as string
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    balance_after DECIMAL(19,2) NOT NULL,
    account_id BIGINT NOT NULL,
    CONSTRAINT fk_account
        FOREIGN KEY(account_id)
        REFERENCES accounts(id)
        ON DELETE CASCADE
);

-- Optional index on account_id for faster lookups
CREATE INDEX idx_entries_account_id ON entries(account_id);
