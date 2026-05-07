CREATE SCHEMA IF NOT EXISTS acc;

-- BIGSERIAL = автогенерация PK; нужно для @GeneratedValue(strategy = GenerationType.IDENTITY)
CREATE TABLE IF NOT EXISTS acc.user_data
(
    user_id    BIGSERIAL PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    name_surename VARCHAR(255) NOT NULL,
    birthdate  DATE         NOT NULL
);

CREATE TABLE IF NOT EXISTS acc.accounts
(
    account_number BIGSERIAL PRIMARY KEY,
    user_id        BIGINT NOT NULL REFERENCES acc.user_data (user_id),
    balance        DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT chk_balance_non_negative CHECK (balance >= 0)
);

