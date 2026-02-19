CREATE TABLE schools (
    school_id UUID CONSTRAINT schools_pk PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    capacity INTEGER NOT NULL
);

CREATE TABLE students (
    student_id UUID CONSTRAINT students_pk PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    school_id UUID
);

CREATE TABLE cases (
    case_id UUID CONSTRAINT cases_pk PRIMARY KEY,
    student_id UUID NOT NULL,
    school_id UUID NOT NULL,
    status VARCHAR(16) NOT NULL CHECK (status in ('PENDING', 'APPROVED', 'REJECTED'))
);

-- students table foreign keys: school deletion should not delete associated students
ALTER TABLE students ADD CONSTRAINT fk_student_school FOREIGN KEY (school_id) REFERENCES schools ON DELETE SET NULL;

-- cases table foreign keys: school or student deletion should delete associated cases
ALTER TABLE cases ADD CONSTRAINT fk_case_school FOREIGN KEY (school_id) REFERENCES schools ON DELETE CASCADE;
ALTER TABLE cases ADD CONSTRAINT fk_case_student FOREIGN KEY (student_id) REFERENCES students ON DELETE CASCADE;

-- schools table constraints:
-- - school names should be unique (case-insensitive)
CREATE UNIQUE INDEX uq_school_name_ci ON schools (LOWER(name));
-- - school capacity limits verified additionally on db level
ALTER TABLE schools ADD CONSTRAINT chk_school_capacity CHECK (capacity BETWEEN 50 AND 2000);