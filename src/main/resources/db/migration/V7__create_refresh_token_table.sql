ALTER TABLE products ALTER COLUMN price TYPE NUMERIC(19, 2);

CREATE TABLE refresh_tokens
(
    jti        UUID PRIMARY KEY         NOT NULL,
    user_id    BIGINT                   NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked    BOOLEAN                  NOT NULL DEFAULT FALSE
);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);