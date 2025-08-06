CREATE EXTENSION IF NOT EXISTS pgcrypto; -- For UUID publicIds

CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    public_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(250) NOT NULL,
    balance NUMERIC(20, 2) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE portfolio (
    id SERIAL PRIMARY KEY,
    public_id VARCHAR(50) NOT NULL UNIQUE,
    account_public_id VARCHAR(50) NOT NULL,
    crypto_symbol VARCHAR(10) NOT NULL,
    amount NUMERIC(20, 10) NOT NULL DEFAULT 0.0,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (crypto_symbol, account_public_id),
    FOREIGN KEY (account_public_id) REFERENCES account(public_id) ON DELETE CASCADE
);

CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    public_id VARCHAR(50) NOT NULL UNIQUE,
    account_public_id VARCHAR(50) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('BUY', 'SELL')),
    crypto_symbol VARCHAR(10) NOT NULL,
    amount NUMERIC(20, 10) NOT NULL,
    price_at_transaction NUMERIC(20, 10) NOT NULL,
    total_value NUMERIC(20, 2) NOT NULL,
    profit_or_loss NUMERIC(20, 2),
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_public_id) REFERENCES account(public_id) ON DELETE CASCADE
);

-- Demo Account
INSERT INTO account (name, balance, public_id) VALUES ('DEMO', 10000.00, gen_random_uuid()::text);