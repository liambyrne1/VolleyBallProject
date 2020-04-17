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
