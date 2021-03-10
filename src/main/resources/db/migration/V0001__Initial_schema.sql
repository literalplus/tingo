-- Initial database schema for Tingo
-- Note that this assumes that the database has already been created and selected.
-- For a MySQL/MariaDB server.

-- tingo_teacher table
-- Note: There's no difference in space usage between using VARCHAR(4) and VARCHAR(255)
CREATE TABLE IF NOT EXISTS tingo_teacher
(
  id SERIAL PRIMARY KEY NOT NULL,
  abbr VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX tingo_teacher_abbr_uindex ON tingo_teacher (abbr);

-- tingo_field table (stores fields by teachers)
CREATE TABLE tingo_field
(
  id SERIAL PRIMARY KEY NOT NULL,
  teacher_id INT NOT NULL,
  text TEXT NOT NULL,
  CONSTRAINT tingo_field_tingo_teacher_id_fk FOREIGN KEY (teacher_id) REFERENCES tingo_teacher (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE INDEX tingo_field_teacher_id_index ON tingo_field (teacher_id);
COMMENT ON TABLE tingo_field IS 'Stores Tingo fields by teacher';
