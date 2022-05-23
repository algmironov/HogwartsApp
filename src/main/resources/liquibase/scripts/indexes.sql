-- liquibase formatted sql

-- changeset algmironov:1
CREATE INDEX students_name_index ON students (name);
CREATE INDEX faculties_name_and_color_index ON faculties (name, color);
