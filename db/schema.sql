CREATE TABLE agents (
    id              UUID PRIMARY KEY,
    latitude        DOUBLE PRECISION NOT NULL,
    longitude       DOUBLE PRECISION NOT NULL,
    h3_cell         BIGINT NOT NULL,
    capacity        INT NOT NULL DEFAULT 1,
    current_load    INT NOT NULL DEFAULT 0,
    status          VARCHAR(10) NOT NULL DEFAULT 'IDLE' CHECK (status IN  ('IDLE', 'BUSY', 'INACTIVE')),
    created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE orders (
    id          UUID PRIMARY KEY,
    latitude    DOUBLE PRECISION NOT NULL,
    longitude   DOUBLE PRECISION NOT NULL,
    h3_cell     BIGINT NOT NULL,
    status      VARCHAR(10) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'MATCHED')),
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE assignments (
    id          UUID PRIMARY KEY,
    agent_id    UUID NOT NULL REFERENCES agents(id),
    order_id    UUID NOT NULL REFERENCES orders(id) UNIQUE,
    matched_at  TIMESTAMP NOT NULL DEFAULT now()
);



