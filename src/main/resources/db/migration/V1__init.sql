-- Create the students table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    city VARCHAR(100) NOT NULL,
    city_time_zone VARCHAR(50) NOT NULL
);

-- Create the exams table
CREATE TABLE exams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_name VARCHAR(100) NOT NULL
);

-- Create the questions table
CREATE TABLE questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    choice1 TEXT NOT NULL,
    choice2 TEXT NOT NULL,
    choice3 TEXT NOT NULL,
    choice4 TEXT NOT NULL,
    correct_choice INT NOT NULL, -- 1 to 4
    score INT NOT NULL, -- 10
    FOREIGN KEY (exam_id) REFERENCES exams(id)
);

-- Create the exam_schedules table
CREATE TABLE exam_schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    start_date_time DATETIME NOT NULL,
    end_date_time DATETIME NOT NULL,
    FOREIGN KEY (exam_id) REFERENCES exams(id),
    FOREIGN KEY (student_id) REFERENCES students(id)
);

-- Create the student_responses table
CREATE TABLE student_responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_schedule_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    student_response INT NOT NULL,
    FOREIGN KEY (exam_schedule_id) REFERENCES exam_schedules(id),
    FOREIGN KEY (question_id) REFERENCES questions(id),
    UNIQUE KEY student_response_uq (exam_schedule_id, question_id)
);

-- Create the exam_grades table
CREATE TABLE exam_grades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_schedule_id BIGINT NOT NULL,
    grade INT NOT NULL,
    FOREIGN KEY (exam_schedule_id) REFERENCES exam_schedules(id)
);
