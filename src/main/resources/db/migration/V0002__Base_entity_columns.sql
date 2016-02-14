-- Adds new columns from BaseEntity (creation_date, last_updated) to existing tables

ALTER TABLE tingo_field ADD creation_date DATETIME NOT NULL;
ALTER TABLE tingo_field ADD last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL;

ALTER TABLE tingo_teacher ADD creation_date DATETIME NOT NULL;
ALTER TABLE tingo_teacher ADD last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL;
