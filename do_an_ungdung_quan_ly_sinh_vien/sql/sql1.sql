create database quanly_sinhvien;
drop database quanly_sinhvien;

create table students(

	id varchar(255) primary key,
    fullname varchar(255),
    birthday datetime,
    code varchar(255)
);

create table classes(

	id varchar(255) primary key,
    name varchar(255)
);

create table subjects(

	id varchar(255) primary key,
    name varchar(255)
);

create table points(

	id varchar(255) primary key,
    point decimal,
    note text,
    student_id varchar(255),
    class_id varchar(255),
    subject_id varchar(255),
	CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_class FOREIGN KEY (class_id) REFERENCES classes(id),
    CONSTRAINT fk_subject FOREIGN KEY (subject_id) REFERENCES subjects(id)
);

INSERT INTO students (id, fullname, birthday, code) VALUES
('S001', 'Nguyen Van A', '2000-01-01', 'SV001'),
('S002', 'Le Thi B', '1999-05-15', 'SV002'),
('S003', 'Tran Van C', '2001-07-20', 'SV003');

INSERT INTO classes (id, name) VALUES
('C001', 'Class 1A'),
('C002', 'Class 2B'),
('C003', 'Class 3C');

INSERT INTO subjects (id, name) VALUES
('SB001', 'Mathematics'),
('SB002', 'Physics'),
('SB003', 'Chemistry');

INSERT INTO points (id, point, note, student_id, class_id, subject_id) VALUES
('P001', 8.5, 'Good performance', 'S001', 'C001', 'SB001'),
('P002', 7.0, 'Average', 'S002', 'C002', 'SB002'),
('P003', 9.0, 'Excellent', 'S003', 'C003', 'SB003');

SELECT * FROM students;
SELECT * FROM students;
SELECT * FROM points;

SELECT * FROM students, points, classes, subjects
WHERE students.id = points.student_id AND
classes.id = points.class_id AND
subjects.id = points.subject_id;














