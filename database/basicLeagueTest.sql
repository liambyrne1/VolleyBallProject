-------------------------------------------------------------------------
-- Target DBMS:           PostgreSQL 11                                 -
-- Project name:          Volleyball                                    -
-- Author:                Liam Byrne                                    -
-- Created on:            2019-03-31                                    -
-------------------------------------------------------------------------
DROP DATABASE IF EXISTS volleyball;

CREATE DATABASE volleyball;

\c volleyball;

-------------------------------------------------------------------------
-- Tables                                                               -
-------------------------------------------------------------------------
-------------------------------------------------------------------------
-- Add table "Leagues"                                                  -
-------------------------------------------------------------------------

CREATE TABLE leagues (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(30) NOT NULL
);

INSERT INTO leagues (name) VALUES ('Ffff01');
INSERT INTO leagues (name) VALUES ('Ffff02');
INSERT INTO leagues (name) VALUES ('Ffff03');
INSERT INTO leagues (name) VALUES ('Ffff04');
INSERT INTO leagues (name) VALUES ('Ffff05');
INSERT INTO leagues (name) VALUES ('Ffff06');
INSERT INTO leagues (name) VALUES ('Ffff07');
INSERT INTO leagues (name) VALUES ('Ffff08');
INSERT INTO leagues (name) VALUES ('Ffff09');
INSERT INTO leagues (name) VALUES ('Ffff10');
INSERT INTO leagues (name) VALUES ('Ffff11');
INSERT INTO leagues (name) VALUES ('Ffff12');
INSERT INTO leagues (name) VALUES ('Ffff13');
INSERT INTO leagues (name) VALUES ('Ffff14');
INSERT INTO leagues (name) VALUES ('Ffff15');
INSERT INTO leagues (name) VALUES ('Ffff16');
INSERT INTO leagues (name) VALUES ('Ffff17');
INSERT INTO leagues (name) VALUES ('Ffff18');
INSERT INTO leagues (name) VALUES ('Ffff19');
INSERT INTO leagues (name) VALUES ('Ffff20');
INSERT INTO leagues (name) VALUES ('Ffff21');
INSERT INTO leagues (name) VALUES ('Ffff22');
INSERT INTO leagues (name) VALUES ('Ffff23');
INSERT INTO leagues (name) VALUES ('Ffff24');
INSERT INTO leagues (name) VALUES ('Ffff25');
INSERT INTO leagues (name) VALUES ('Ffff26');
INSERT INTO leagues (name) VALUES ('Ffff27');
INSERT INTO leagues (name) VALUES ('Ffff28');
INSERT INTO leagues (name) VALUES ('Ffff29');
INSERT INTO leagues (name) VALUES ('Ffff30');
