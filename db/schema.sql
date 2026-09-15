
CREATE TABLE users (
    id          UUID PRIMARY KEY,
    email       VARCHAR(100) UNIQUE,
    password    VARCHAR(100),
    role        VARCHAR(10) NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'AGENT', 'ADMIN')),
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);


CREATE TABLE agents (
    id              UUID PRIMARY KEY,
    user_id         UUID NOT NULL REFERENCES users(id) UNIQUE,
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
    user_id     UUID NOT NULL REFERENCES users(id),
    latitude    DOUBLE PRECISION NOT NULL,
    longitude   DOUBLE PRECISION NOT NULL,
    h3_cell     BIGINT NOT NULL,
    status      VARCHAR(10) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'MATCHED')),
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE assignments (
    id          UUID PRIMARY KEY,
    agent_id    UUID NOT NULL REFERENCES agents(id),
    order_id    UUID NOT NULL REFERENCES orders(id), -- this is not made unique to supposrt cancellations and reassignments
    matched_at  TIMESTAMP NOT NULL DEFAULT now()
);




