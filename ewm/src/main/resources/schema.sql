-- DROP TABLE IF EXISTS compilation_event, compilations, participation_requests, events, categories, users CASCADE;

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name varchar(256)                            NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  varchar(256)                            NOT NULL,
    email varchar(256)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         TEXT,
    category_id        BIGINT                                  NOT NULL,
    created_on         TIMESTAMP,
    description        TEXT,
    event_date         TIMESTAMP,
    initiator_id       BIGINT                                  NOT NULL,
    lat                DOUBLE PRECISION,
    lon                DOUBLE PRECISION,
    paid               BOOLEAN,
    participant_limit  BIGINT,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN,
    state              varchar(50),
    title              varchar(256),

    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_initiator_id FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories (id)
);



CREATE TABLE IF NOT EXISTS participation_requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    state        varchar(50),
    created      TIMESTAMP,

    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requester_id FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (id)
);


CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  varchar(256),
    pinned BOOLEAN,

    CONSTRAINT pk_compilations PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS compilation_event
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,

    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fr_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT pk_compilation_event PRIMARY KEY (compilation_id, event_id)
);

create table IF NOT EXISTS revinfo
(
    rev      integer not null
        primary key,
    revtstmp bigint
);

create table IF NOT EXISTS categories_aud
(
    id      bigint  not null,
    rev     integer not null
        constraint fk6ti58h8w0q47oacscu06hcite
            references revinfo,
    revtype smallint,
    name    varchar(255),
    primary key (id, rev),
    CONSTRAINT fk_rev_id FOREIGN KEY (rev) REFERENCES revinfo (rev)

);

