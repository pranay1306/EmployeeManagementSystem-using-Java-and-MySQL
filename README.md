# Employee Management System

## Overview
This is a Java-based Employee Management System with a Swing GUI and MySQL database connectivity. It allows users to add, update, delete, and view employee records.

## Features
- Add new employees
- Update existing employee details
- Delete employees from the database
- View all employees in a table
- Interactive GUI using Swing
- MySQL database integration

## Technologies Used
- Java (Swing for GUI)
- MySQL (JDBC for database connectivity)
- Maven (for dependency management, if needed)

## Setup Instructions
### Prerequisites
- Install Java Development Kit (JDK)
- Install MySQL Server
- Add MySQL JDBC Driver to the classpath

### Database Configuration
Create a MySQL database and table using the following SQL script:

```sql
CREATE DATABASE EmployeeDB;
USE EmployeeDB;

CREATE TABLE Employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    department VARCHAR(100) NOT NULL,
    salary DOUBLE NOT NULL
);
```

### How to Run
1. Clone this repository:
   ```sh
   git clone https://github.com/pranay1306/EmployeeManagementSystem-using-Java-and-MySQL.git
   cd EmployeeManagementSystem-using-Java-and-MySQL
   ```

2. Compile and run the program:
   ```sh
   javac EmployeeManagementSystem.java
   java EmployeeManagementSystem
   ```

## Screenshots
(Add screenshots of the application here)

