DELETE FROM payments;
DELETE FROM schedules;
DELETE FROM admissions;
DELETE FROM courses;
DELETE FROM students;
DELETE FROM users;
DELETE FROM sequences;

insert into sequences (table_name,pattern,create_count) values ("some_table","ST2021",1);

insert into sequences (table_name,pattern,create_count) VALUES ("students","STUD2020",5),("students","STUD2021",5);
insert into students (student_id,given_name,family_name,sex,photo_url,date_of_birth,address,phone,email,date_of_join)
values
("STUD20201","gn1","fn1","MALE","photo1","1997-12-03","address1","phone1","email1","2020-04-05"),
("STUD20202","gn3","fn3","FEMALE","photo3","1989-03-21","address3","phone3","email3","2020-04-05"),
("STUD20203","gn4","fn4","OTHER","photo4","1995-12-06","address4","phone4","email4","2020-10-12"),
("STUD20204","gn6","fn6","MALE","photo6","1997-11-05","address7","phone6","email6","2020-12-21"),
("STUD20205","gn9","fn9","OTHER","photo9","1999-04-11","address9","phone9","email9","2020-03-15"),
("STUD20211","gn5","fn5","MALE","photo5","1993-08-03","address5","phone5","email5","2021-01-21"),
("STUD20212","gn7","fn7","FEMALE","photo7","1990-10-03","address7","phone7","email7","2021-04-05"),
("STUD20213","gn8","fn8","OTHER","photo8","1998-12-03","address8","phone8","email8","2021-04-05"),
("STUD20214","gn2","fn2","FEMALE","photo2","1997-10-21","address2","phone2","email2","2021-05-03"),
("STUD20215","gn10","fn10","FEMALE","photo10","1990-10-21","address10","phone10","email10","2021-03-01");

insert into sequences (table_name,pattern,create_count) VALUES ("courses","C2020",3),("courses","C2021",4);
insert into courses (course_id,name,description,fees,status,course_start,course_end,total_seats,available_seats)
values
("C20201","name 1","description 1",100,"COMPLETE","2020-04-15","2020-07-05",2,0),
("C20202","name 2","description 2",160,"RUNNING","2020-12-30","2020-09-01",4,0),
("C20203","name 3","description 3",120,"CANCEL","2020-04-15","2020-07-05",3,1),
("C20211","name 4","description 4",200,"OPEN","2021-07-01","2021-12-01",7,4),
("C20212","name 5","description 5",220,"CLOSE","2021-06-22","2021-08-22",2,1),
("C20213","name 6","description 6",130,"RUNNING","2021-04-15","2021-10-01",3,0);

insert into schedules (schedule_id,course_id,start,end,description)
values
("S1","C20201","2020-04-15 10:00:00","2020-04-15 11:00:00","class 1"),
("S2","C20201","2020-06-12 10:00:00","2020-06-12 11:00:00","class 2"),
("S3","C20201","2020-07-05 10:00:00","2020-07-05 11:00:00","class 3"),

("S4","C20202","2020-12-30 11:30:00","2020-12-30 12:30:00","class 1"),
("S5","C20202","2021-02-28 11:30:00","2021-02-28 12:30:00","class 2"),
("S6","C20202","2021-03-12 11:30:00","2021-03-12 12:30:00","class 3"),
("S7","C20202","2021-04-15 11:30:00","2021-04-15 12:30:00","class 4"),
("S8","C20202","2021-05-05 11:30:00","2021-05-05 12:30:00","class 5"),
("S9","C20202","2021-06-19 11:30:00","2021-06-19 12:30:00","class 6"),

("S10","C20213","2021-04-15 08:30:00","2021-04-15 10:00:00","class 1"),
("S11","C20213","2021-05-15 08:30:00","2021-05-15 10:00:00","class 2"),
("S12","C20213","2021-06-01 08:30:00","2021-06-01 10:00:00","class 3");

insert into admissions (admission_id,student_id,course_id,admission_date,status,net_payable,due_payment)
values
("A1","STUD20201","C20201","2020-04-09","ENROLLED",100,0),
("A2","STUD20202","C20201","2020-04-11","ENROLLED",100,0),

("A3","STUD20201","C20202","2020-12-01","ENROLLED",160,20),
("A4","STUD20203","C20202","2020-12-11","ENROLLED",160,0),
("A5","STUD20204","C20202","2020-12-28","ENROLLED",160,0),
("A6","STUD20205","C20202","2020-12-10","ENROLLED",160,60),

("A7","STUD20205","C20203","2020-04-05","ENROLLED",120,20),

("A9","STUD20214","C20211","2020-06-01","ENROLLED",200,0),
("A10","STUD20213","C20211","2020-05-28","ENROLLED",200,0),
("A11","STUD20203","C20211","2020-03-31","ENROLLED",200,0),
("A12","STUD20204","C20211","2020-05-30","DISENROLLED",200,0),

("A13","STUD20211","C20212","2020-03-31","ENROLLED",220,0),
("A14","STUD20201","C20212","2020-05-05","DISENROLLED",220,200),

("A15","STUD20205","C20213","2020-03-28","ENROLLED",130,0),
("A16","STUD20212","C20213","2021-04-05","DISENROLLED",130,0),
("A17","STUD20213","C20213","2021-04-10","ENROLLED",130,0),
("A18","STUD20201","C20213","2020-03-12","ENROLLED",130,30);

insert into payments (payment_id,admission_id,student_id,course_id,payment_datetime,amount)
values
("P1","A1","STUD20201","C20201","2020-04-09 11:00:00",100),
("P2","A2","STUD20202","C20201","2020-04-11 11:00:00",100),

("P3","A3","STUD20201","C20202","2020-12-01 11:00:00",70),
("P4","A3","STUD20201","C20202","2021-01-20 11:00:00",30),
("P5","A3","STUD20201","C20202","2021-03-30 11:00:00",40),

("P6","A4","STUD20203","C20202","2020-12-11 11:00:00",160),
("P7","A5","STUD20204","C20202","2020-12-28 11:00:00",160),
("P8","A6","STUD20205","C20202","2020-12-10 11:00:00",100),

("P9","A7","STUD20205","C20203","2020-04-05 11:00:00",50),
("P10","A7","STUD20205","C20203","2020-04-05 11:00:00",50),

("P12","A9","STUD20214","C20211","2020-06-01 11:00:00",200),
("P13","A10","STUD20213","C20211","2020-05-28 11:00:00",200),
("P14","A11","STUD20203","C20211","2020-03-31 11:00:00",200),
("P15","A12","STUD20204","C20211","2020-05-30 11:00:00",200),

("P16","A13","STUD20211","C20212","2020-03-31 11:00:00",220),
("P17","A14","STUD20201","C20212","2020-05-05 11:00:00",20),

("P18","A15","STUD20205","C20213","2020-03-28 11:00:00",130),
("P19","A16","STUD20212","C20213","2021-04-05 11:00:00",130),
("P20","A17","STUD20213","C20213","2021-04-10 11:00:00",130),
("P21","A18","STUD20201","C20213","2020-03-12 11:00:00",100);