-- This adds user abd authority tables for use with Spring Security's default JDBC thing

CREATE TABLE tingo_user
(
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(60) NOT NULL,
  enabled BOOLEAN DEFAULT true NOT NULL
);

CREATE TABLE tingo_authority
(
  id serial NOT NULL PRIMARY KEY, -- so that we can use this with Spring's repository API
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  CONSTRAINT tingo_authority_tingo_user_username_fk FOREIGN KEY (username) REFERENCES tingo_user (username) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE UNIQUE INDEX tingo_authority_username_authority_uindex ON tingo_authority (username, authority);
