CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id SERIAL PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    participant_limit INT NOT NULL,
    date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location_lon FLOAT NOT NULL,
    location_lat FLOAT NOT NULL,
    paid BOOLEAN NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    state VARCHAR(30) NOT NULL,
    category_id INT REFERENCES categories NOT NULL,
    initiator_id INT REFERENCES users NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published TIMESTAMP WITHOUT TIME ZONE,
    confirmed_requests INT NOT NULL
);

CREATE TABLE IF NOT EXISTS requests (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status VARCHAR(30) NOT NULL,
    event_id INT REFERENCES events NOT NULL,
    requester_id INT REFERENCES users NOT NULL,
    UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id SERIAL PRIMARY KEY,
    title VARCHAR (50) NOT NULL,
    pinned BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS events_compilations (
    event_id INT REFERENCES events,
    compilation_id INT REFERENCES compilations,
    PRIMARY KEY (event_id, compilation_id)
)