CREATE TABLE users
(
    id INTEGER PRIMARY KEY NOT NULL,
    depcode VARCHAR(20),
    depjob VARCHAR(100),
    description VARCHAR(255)
);
CREATE UNIQUE INDEX users_depcode_depjob_key ON users (depcode, depjob);
CREATE SEQUENCE user_ids;

