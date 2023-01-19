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

INSERT INTO teams (name, club_id) VALUES ('T101', 1);
INSERT INTO teams (name, club_id, league_id) VALUES ('T102', 1, 1);

INSERT INTO teams (name, club_id) VALUES ('T201', 2);
INSERT INTO teams (name, club_id, league_id) VALUES ('T202', 2, 2);
INSERT INTO teams (name, club_id, league_id) VALUES ('T203', 2, 2);
INSERT INTO teams (name, club_id, league_id) VALUES ('T204', 2, 2);

INSERT INTO teams (name, club_id) VALUES ('T301', 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('T302', 3, 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('T303', 3, 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('T304', 3, 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('T305', 3, 3);
INSERT INTO teams (name, club_id, league_id) VALUES ('T306', 3, 3);

INSERT INTO teams (name, club_id) VALUES ('T401', 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('T402', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('T403', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('T404', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('T405', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('T406', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('T407', 4, 4);
INSERT INTO teams (name, club_id, league_id) VALUES ('T408', 4, 4);

INSERT INTO teams (name, club_id) VALUES ('T501', 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T502', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T503', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T504', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T505', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T506', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T507', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T508', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T509', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T510', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T511', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T512', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T513', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T514', 5, 5);
INSERT INTO teams (name, club_id, league_id) VALUES ('T515', 5, 5);

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
