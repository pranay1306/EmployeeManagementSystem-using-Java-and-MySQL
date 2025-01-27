CREATE DATABASE EmployeeDB;

USE EmployeeDB;

CREATE TABLE Employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    department VARCHAR(100),
    salary DECIMAL(10, 2)
);
INSERT INTO Employees (name, age, department, salary)
VALUES ('John Doe', 30, 'IT', 50000.00);
