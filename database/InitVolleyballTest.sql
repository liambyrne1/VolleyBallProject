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

INSERT INTO leagues (name) VALUES ('League 01');
INSERT INTO leagues (name) VALUES ('League 02');
INSERT INTO leagues (name) VALUES ('League 03');
INSERT INTO leagues (name) VALUES ('League 04');
INSERT INTO leagues (name) VALUES ('League 05');
INSERT INTO leagues (name) VALUES ('League 06');
INSERT INTO leagues (name) VALUES ('League 07');
INSERT INTO leagues (name) VALUES ('League 08');
INSERT INTO leagues (name) VALUES ('League 09');
INSERT INTO leagues (name) VALUES ('League 10');
INSERT INTO leagues (name) VALUES ('League 11');
INSERT INTO leagues (name) VALUES ('League 12');
INSERT INTO leagues (name) VALUES ('League 13');
INSERT INTO leagues (name) VALUES ('League 14');
INSERT INTO leagues (name) VALUES ('League 15');

-------------------------------------------------------------------------
-- Add table "Clubs"                                                  -
-------------------------------------------------------------------------

CREATE TABLE clubs (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(30) NOT NULL
);

INSERT INTO clubs (name) VALUES ('Club 01');
INSERT INTO clubs (name) VALUES ('Club 02');
INSERT INTO clubs (name) VALUES ('Club 03');
INSERT INTO clubs (name) VALUES ('Club 04');
INSERT INTO clubs (name) VALUES ('Club 05');
INSERT INTO clubs (name) VALUES ('Club 06');
INSERT INTO clubs (name) VALUES ('Club 07');
INSERT INTO clubs (name) VALUES ('Club 08');
INSERT INTO clubs (name) VALUES ('Club 09');
INSERT INTO clubs (name) VALUES ('Club 10');
INSERT INTO clubs (name) VALUES ('Club 11');
INSERT INTO clubs (name) VALUES ('Club 12');
INSERT INTO clubs (name) VALUES ('Club 13');
INSERT INTO clubs (name) VALUES ('Club 14');
INSERT INTO clubs (name) VALUES ('Club 15');

-------------------------------------------------------------------------
-- Add table "Teams"                                                  -
-------------------------------------------------------------------------

CREATE TABLE teams (
    id SERIAL NOT NULL,
    name VARCHAR(30) NOT NULL,
    club_id INT NOT NULL,
    league_id INT,
    PRIMARY KEY (id),
    CONSTRAINT club_fk
      FOREIGN KEY (club_id)
        REFERENCES clubs (id)
        ON DELETE CASCADE,
    CONSTRAINT league_fk
      FOREIGN KEY (league_id)
        REFERENCES leagues (id)
);

INSERT INTO teams (name, club_id) VALUES ('Team 101', 1);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 102', 1, 1);

INSERT INTO teams (name, club_id) VALUES ('Team 201', 2);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 202', 2, 2);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 203', 2, 2);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 204', 2, 2);

INSERT INTO teams (name, club_id) VALUES ('Team 301', 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 302', 3, 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 303', 3, 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 304', 3, 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 305', 3, 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 306', 3, 3);

INSERT INTO teams (name, club_id) VALUES ('Team 401', 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 402', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 403', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 404', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 405', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 406', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 407', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 408', 4, 4);

INSERT INTO teams (name, club_id) VALUES ('Team 501', 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 502', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 503', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 504', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 505', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 506', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 507', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 508', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 509', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 510', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 511', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 512', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 513', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 514', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('Team 515', 5, 5);

-------------------------------------------------------------------------
-- Add table "users"                                                  -
-------------------------------------------------------------------------

CREATE TABLE users (
  first_name VARCHAR(40) NOT NULL,
  last_name VARCHAR(40) NOT NULL,
  email VARCHAR(40) NOT NULL,
  password VARCHAR(80) NOT NULL,
  PRIMARY KEY (email)
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
  email VARCHAR(40) NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (email, role_id),
  CONSTRAINT role_fk
    FOREIGN KEY (role_id)
      REFERENCES roles (role_id)
      ON DELETE CASCADE,
  CONSTRAINT user_fk
    FOREIGN KEY (email)
      REFERENCES users (email)
      ON DELETE CASCADE
);

INSERT INTO roles (name)
VALUES ('USER');

INSERT INTO roles (name)
VALUES ('ADMIN');

INSERT INTO users VALUES ('Firsta', 'Lasta', 'a.b@b.bb', '$2a$10$z72HDPl/S5K5sJw99zvC3.6/aNWIPV82oP.VgTZAX/Y7VIgYE0x.2');
INSERT INTO users VALUES ('Firstb', 'Lastb', 'b.b@b.bb', '$2a$10$z72HDPl/S5K5sJw99zvC3.6/aNWIPV82oP.VgTZAX/Y7VIgYE0x.2');
INSERT INTO users VALUES ('Firstc', 'Lastc', 'c.b@b.bb', '$2a$10$z72HDPl/S5K5sJw99zvC3.6/aNWIPV82oP.VgTZAX/Y7VIgYE0x.2');
INSERT INTO users VALUES ('Firstd', 'Lastd', 'd.b@b.bb', '$2a$10$z72HDPl/S5K5sJw99zvC3.6/aNWIPV82oP.VgTZAX/Y7VIgYE0x.2');

INSERT INTO users_roles VALUES ('a.b@b.bb', 1);
INSERT INTO users_roles VALUES ('a.b@b.bb', 2);
INSERT INTO users_roles VALUES ('b.b@b.bb', 1);
INSERT INTO users_roles VALUES ('c.b@b.bb', 1);
INSERT INTO users_roles VALUES ('d.b@b.bb', 2);
