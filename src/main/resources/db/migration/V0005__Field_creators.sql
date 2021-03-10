ALTER TABLE tingo_field ADD creator_name VARCHAR(50) DEFAULT NULL NULL;
ALTER TABLE tingo_field
    ADD CONSTRAINT tingo_field_tingo_user_username_fk
    FOREIGN KEY (creator_name) REFERENCES tingo_user (username) ON DELETE SET NULL ON UPDATE CASCADE;
