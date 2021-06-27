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

INSERT INTO leagues (name) VALUES ('League 10');
INSERT INTO leagues (name) VALUES ('League 09');
INSERT INTO leagues (name) VALUES ('League 08');
INSERT INTO leagues (name) VALUES ('League 07');
INSERT INTO leagues (name) VALUES ('League 06');
INSERT INTO leagues (name) VALUES ('League 05');
INSERT INTO leagues (name) VALUES ('League 04');
INSERT INTO leagues (name) VALUES ('League 03');
INSERT INTO leagues (name) VALUES ('League 02');
INSERT INTO leagues (name) VALUES ('League 01');
INSERT INTO leagues (name) VALUES ('Men''s Division 03 - (South)');

-------------------------------------------------------------------------
-- Add table "Clubs"                                                  -
-------------------------------------------------------------------------

CREATE TABLE clubs (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(30) NOT NULL
);

INSERT INTO clubs (name) VALUES ('Club 10');
INSERT INTO clubs (name) VALUES ('Club 09');
INSERT INTO clubs (name) VALUES ('Club 08');
INSERT INTO clubs (name) VALUES ('Club 07');
INSERT INTO clubs (name) VALUES ('Club 06');
INSERT INTO clubs (name) VALUES ('Club 05');
INSERT INTO clubs (name) VALUES ('Club 04');
INSERT INTO clubs (name) VALUES ('Club 03');
INSERT INTO clubs (name) VALUES ('Club 02');
INSERT INTO clubs (name) VALUES ('Club 01');
INSERT INTO clubs (name) VALUES ('Men''s Club1234 03 - (South)');

-------------------------------------------------------------------------
-- Add table "users"                                                  -
-------------------------------------------------------------------------

CREATE TABLE users (
  username VARCHAR(40) NOT NULL,
  password VARCHAR(80) NOT NULL,
  PRIMARY KEY (username)
);

-------------------------------------------------------------------------
-- Add table "roles"                                                  -
-------------------------------------------------------------------------

CREATE TABLE roles (
  role_id SERIAL NOT NULL,
  name varchar(20) NOT NULL,
  PRIMARY KEY (role_id)
);

-------------------------------------------------------------------------
-- Add table "users_roles"                                                  -
-------------------------------------------------------------------------

CREATE TABLE users_roles (
  username VARCHAR(40) NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (username, role_id),
  CONSTRAINT role_fk
    FOREIGN KEY (role_id)
      REFERENCES roles (role_id)
      ON DELETE CASCADE,
  CONSTRAINT user_fk
    FOREIGN KEY (username)
      REFERENCES users (username)
      ON DELETE CASCADE
);

INSERT INTO roles (name)
VALUES ('USER');

INSERT INTO roles (name)
VALUES ('ADMIN');

INSERT INTO users VALUES ('a', '$2a$10$5q7032AQT4Y.qo6esr2TGOtTVNOIG7bB7fNrN/6buZdOsCZs.t/su');
INSERT INTO users VALUES ('b', '$2a$10$5q7032AQT4Y.qo6esr2TGOtTVNOIG7bB7fNrN/6buZdOsCZs.t/su');
INSERT INTO users VALUES ('c', '$2a$10$5q7032AQT4Y.qo6esr2TGOtTVNOIG7bB7fNrN/6buZdOsCZs.t/su');
INSERT INTO users VALUES ('d', '$2a$10$5q7032AQT4Y.qo6esr2TGOtTVNOIG7bB7fNrN/6buZdOsCZs.t/su');

INSERT INTO users_roles VALUES ('a', 1);
INSERT INTO users_roles VALUES ('a', 2);
INSERT INTO users_roles VALUES ('b', 1);
INSERT INTO users_roles VALUES ('c', 1);
INSERT INTO users_roles VALUES ('d', 2);
