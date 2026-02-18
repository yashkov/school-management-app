CREATE TABLE schools (
    school_id UUID CONSTRAINT schools_pk PRIMARY KEY,
    name varchar(255) UNIQUE NOT NULL,
    capacity bigint NOT NULL
);

CREATE TABLE students (
    student_id UUID CONSTRAINT students_pk PRIMARY KEY,
    name varchar(255) NOT NULL,
    school_id UUID
);

ALTER TABLE students ADD CONSTRAINT fk_student_school FOREIGN KEY (school_id) REFERENCES schools ON DELETE SET NULL;