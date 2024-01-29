[![CI with Maven](https://github.com/Constantine-M/job4j_todo/actions/workflows/gitActions.yml/badge.svg)](https://github.com/Constantine-M/job4j_todo/actions/workflows/gitActions.yml)

# Project "TODO"

About project
-------------
This is a classic TODO web application with basic functionality:

- add task
- update task (in progress -> complete)
- delete task
- display only completed tasks/only new tasks/all tasks


Tech stack.
--------------
- Java 17
- PostgreSQL 14
- Spring Boot 2
- Hibernate 5
- Thymeleaf
- Bootstrap
- Liquibase
- Lombok

Environment requirements.
------------------------
1. Java 17
2. Maven 3.8
3. PostgreSQL 14

Launching the project.
---------------------
1. Recommended Chrome based browser.
2. Install PostgreSQL: login - postgres, password - password;
3. Create cinema database;
> CREATE DATABASE todo;
4. Build the project and run the Spring Boot application
> mvn clean package spring-boot:run

Application Interaction.
------------------------
------------------------
Main
----
![img.png](files/Main.png)

List of all tasks
--------
![img.png](files/allTask.png)

Filter to choose task type
-------
![img.png](files/Filter.png)

All completed tasks
------------
![img.png](files/completed.png)

All new tasks
-----------
![img.png](files/newTask.png)

Create task
----------
![img.png](files/create.png)

Edit task
----------
![img.png](files/edit.png)

Contacts.
--------
- Telegram - https://t.me/ConstaMezenin.