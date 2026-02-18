CREATE TABLE schools (
    school_id UUID CONSTRAINT schools_pk PRIMARY KEY,
    name varchar(255) NOT NULL,
    capacity integer NOT NULL
);

CREATE TABLE students (
    student_id UUID CONSTRAINT students_pk PRIMARY KEY,
    name varchar(255) NOT NULL,
    school_id UUID
);

ALTER TABLE students ADD CONSTRAINT fk_student_school FOREIGN KEY (school_id) REFERENCES schools ON DELETE SET NULL;

-- case-insensitive uniqueness of school names
CREATE UNIQUE INDEX IF NOT EXISTS uq_school_name_ci ON schools (LOWER(name));

-- allowed school capacity
ALTER TABLE schools ADD CONSTRAINT chk_school_capacity CHECK (capacity BETWEEN 50 AND 2000);