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

INSERT INTO leagues (name) VALUES ('L01');
INSERT INTO leagues (name) VALUES ('L02');
INSERT INTO leagues (name) VALUES ('L03');
INSERT INTO leagues (name) VALUES ('L04');
INSERT INTO leagues (name) VALUES ('L05');
INSERT INTO leagues (name) VALUES ('L06');
INSERT INTO leagues (name) VALUES ('L07');
INSERT INTO leagues (name) VALUES ('L08');
INSERT INTO leagues (name) VALUES ('L09');
INSERT INTO leagues (name) VALUES ('L10');
INSERT INTO leagues (name) VALUES ('L11');
INSERT INTO leagues (name) VALUES ('L12');
INSERT INTO leagues (name) VALUES ('L13');
INSERT INTO leagues (name) VALUES ('L14');
INSERT INTO leagues (name) VALUES ('L15');

-------------------------------------------------------------------------
-- Add table "Clubs"                                                  -
-------------------------------------------------------------------------

CREATE TABLE clubs (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(30) NOT NULL
);

INSERT INTO clubs (name) VALUES ('C01');
INSERT INTO clubs (name) VALUES ('C02');
INSERT INTO clubs (name) VALUES ('C03');
INSERT INTO clubs (name) VALUES ('C04');
INSERT INTO clubs (name) VALUES ('C05');
INSERT INTO clubs (name) VALUES ('C06');
INSERT INTO clubs (name) VALUES ('C07');
INSERT INTO clubs (name) VALUES ('C08');
INSERT INTO clubs (name) VALUES ('C09');
INSERT INTO clubs (name) VALUES ('C10');
INSERT INTO clubs (name) VALUES ('C11');
INSERT INTO clubs (name) VALUES ('C12');
INSERT INTO clubs (name) VALUES ('C13');
INSERT INTO clubs (name) VALUES ('C14');
INSERT INTO clubs (name) VALUES ('C15');

-------------------------------------------------------------------------
-- Add table "Teams"                                                  -
-------------------------------------------------------------------------

CREATE TABLE teams (
    id SERIAL NOT NULL,
    name VARCHAR(30) NOT NULL,
    club_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT club_fk
      FOREIGN KEY (club_id)
        REFERENCES clubs (id)
        ON DELETE CASCADE
);

INSERT INTO teams (name, club_id) VALUES ('T101', 1);
INSERT INTO teams (name, club_id) VALUES ('T102', 1);

INSERT INTO teams (name, club_id) VALUES ('T201', 2);
INSERT INTO teams (name, club_id) VALUES ('T202', 2);
INSERT INTO teams (name, club_id) VALUES ('T203', 2);
INSERT INTO teams (name, club_id) VALUES ('T204', 2);

INSERT INTO teams (name, club_id) VALUES ('T301', 3);
INSERT INTO teams (name, club_id) VALUES ('T302', 3);
INSERT INTO teams (name, club_id) VALUES ('T303', 3);
INSERT INTO teams (name, club_id) VALUES ('T304', 3);
INSERT INTO teams (name, club_id) VALUES ('T305', 3);
INSERT INTO teams (name, club_id) VALUES ('T306', 3);

INSERT INTO teams (name, club_id) VALUES ('T401', 4);
INSERT INTO teams (name, club_id) VALUES ('T402', 4);
INSERT INTO teams (name, club_id) VALUES ('T403', 4);
INSERT INTO teams (name, club_id) VALUES ('T404', 4);
INSERT INTO teams (name, club_id) VALUES ('T405', 4);
INSERT INTO teams (name, club_id) VALUES ('T406', 4);
INSERT INTO teams (name, club_id) VALUES ('T407', 4);
INSERT INTO teams (name, club_id) VALUES ('T408', 4);

INSERT INTO teams (name, club_id) VALUES ('T501', 5);
INSERT INTO teams (name, club_id) VALUES ('T502', 5);
INSERT INTO teams (name, club_id) VALUES ('T503', 5);
INSERT INTO teams (name, club_id) VALUES ('T504', 5);
INSERT INTO teams (name, club_id) VALUES ('T505', 5);
INSERT INTO teams (name, club_id) VALUES ('T506', 5);
INSERT INTO teams (name, club_id) VALUES ('T507', 5);
INSERT INTO teams (name, club_id) VALUES ('T508', 5);
INSERT INTO teams (name, club_id) VALUES ('T509', 5);
INSERT INTO teams (name, club_id) VALUES ('T510', 5);
INSERT INTO teams (name, club_id) VALUES ('T511', 5);
INSERT INTO teams (name, club_id) VALUES ('T512', 5);
INSERT INTO teams (name, club_id) VALUES ('T513', 5);
INSERT INTO teams (name, club_id) VALUES ('T514', 5);
INSERT INTO teams (name, club_id) VALUES ('T515', 5);

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
