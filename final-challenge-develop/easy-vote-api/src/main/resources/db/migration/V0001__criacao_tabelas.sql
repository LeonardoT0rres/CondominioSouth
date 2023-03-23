CREATE TABLE IF NOT EXISTS tb_roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS tb_users
(
    id               SERIAL PRIMARY KEY,
    password         VARCHAR(100),
    name             VARCHAR(50),
    email            VARCHAR(50),
    status           VARCHAR(20),
    password_default INTEGER,
    apartment        INTEGER,
    role_id          INTEGER,

    CONSTRAINT FK_users_roles FOREIGN KEY (role_id) REFERENCES tb_roles (id)
);

CREATE TABLE IF NOT EXISTS tb_assemblies
(
    id          SERIAL PRIMARY KEY,
    cardinality VARCHAR(20),
    locale      VARCHAR(50),
    name        VARCHAR(100),
    obs         VARCHAR(250),
    status      VARCHAR(20),
    start       TIMESTAMP,
    finish      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tb_schedules
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(100),
    description    VARCHAR(250),
    status         VARCHAR(20),
    start_time     TIMESTAMP,
    end_time       TIMESTAMP,
    duration       BIGINT,
    positive_votes INTEGER,
    negative_votes INTEGER,
    schedule_order INTEGER,
    assembly_id    INTEGER,

    CONSTRAINT FK_schedules_assembly FOREIGN KEY (assembly_id) REFERENCES tb_assemblies (id)
);

CREATE TABLE IF NOT EXISTS tb_votes
(
    id          SERIAL PRIMARY KEY,
    vote        VARCHAR,
    user_id     INTEGER,
    schedule_id INTEGER,

    CONSTRAINT FK_vote_user FOREIGN KEY (user_id) REFERENCES tb_users (id),
    CONSTRAINT FK_vote_schedule FOREIGN KEY (schedule_id) REFERENCES tb_schedules (id)
);