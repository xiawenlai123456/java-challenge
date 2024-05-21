DROP TABLE IF EXISTS EMPLOYEE;
 
CREATE TABLE EMPLOYEE (
  id LONG AUTO_INCREMENT  PRIMARY KEY,
  department VARCHAR(250) NOT NULL,
  employee_name VARCHAR(250) NOT NULL UNIQUE,
  employee_salary INT DEFAULT NULL
);

DROP TABLE IF EXISTS LOGINUSER;
 
CREATE TABLE LOGINUSER (
  id LONG AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);

INSERT INTO  EMPLOYEE (id, department, employee_name,employee_salary) VALUES
   (1000,'Director', 'Ada',10000000),
   (1001,'Director', 'Jones',10000000),
   (1002, 'HR', 'Jay',18000),
   (1003, 'IT', 'Tom',10000),
   (1004, 'PG', 'Mark',18000),
   (1005, 'PG', 'James',18000);
   
   
INSERT INTO  LOGINUSER (id, name, password) VALUES
   (1000, 'Ada','1234567'),
   (1001, 'Jones','1234567'),
   (1002, 'Jay','1234567'),
   (1003,'Tom','1234567'),
   (1004,'Mark','1234567'),
   (1005,'James','1234567');
