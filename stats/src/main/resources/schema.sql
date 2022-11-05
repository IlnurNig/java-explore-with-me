DROP TABLE IF EXISTS stats CASCADE;

CREATE TABLE IF NOT EXISTS stats
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    app
    varchar
(
    250
) NOT NULL,
    uri varchar
(
    250
) NOT NULL,
    ip varchar
(
    250
) NOT NULL,
    created_on TIMESTAMP,
    CONSTRAINT pk_stats PRIMARY KEY
(
    id
)
    );
