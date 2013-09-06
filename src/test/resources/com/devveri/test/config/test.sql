CREATE DATABASE IF NOT EXISTS test;
USE test;

CREATE TABLE pet (name VARCHAR(20), owner VARCHAR(20), species VARCHAR(20), sex CHAR(1), birth DATE, death DATE);

INSERT INTO pet (name, owner, species, sex, birth, death) VALUES ('Luke', 'Vader', 'dog', 'M', '1970-01-01', '1990-12-31');
INSERT INTO pet (name, owner, species, sex, birth, death) VALUES ('Leia', 'Vader', 'cat', 'F', '1970-01-01', '1990-12-31');
