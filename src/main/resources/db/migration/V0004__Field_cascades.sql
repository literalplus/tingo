-- We had restrict before, although we want deleting teachers to delete fields too.

ALTER TABLE tingo_field DROP FOREIGN KEY tingo_field_tingo_teacher_id_fk;
ALTER TABLE tingo_field
ADD CONSTRAINT tingo_field_tingo_teacher_id_fk
FOREIGN KEY (teacher_id) REFERENCES tingo_teacher (id) ON DELETE CASCADE ON UPDATE CASCADE;
