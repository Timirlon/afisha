CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    annotation VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    participant_limit INT NOT NULL,
    date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location_lon INT NOT NULL,
    location_lan INT NOT NULL,
    paid BOOLEAN NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    category_id INT REFERENCES categories NOT NULL,
    initiator_id INT REFERENCES users NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published TIMESTAMP WITHOUT TIME ZONE,
    views INT,
    confirmed_requests INT
);

CREATE TABLE IF NOT EXISTS requests (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status VARCHAR(30),
    event_id INT REFERENCES events NOT NULL,
    requester_id INT REFERENCES users NOT NULL,
    UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id SERIAL PRIMARY KEY,
    title VARCHAR (100) NOT NULL,
    pinned BOOLEAN NOT NULL
);

CREATE TABLE events_compilations (
    event_id INT REFERENCES events,
    compilation_id INT REFERENCES compilations,
    PRIMARY KEY (event_id, compilation_id)
)