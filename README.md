# CMPE-275 Lab 2 REST APIs, Persistence, Transactions

### Steps to Run Application
- Make sure Java JDK and Gradle are setup.
- Run following command.
```
$ gradlew bootRun
```

### Local Database Setup
- Make sure Docker and MySQL Workbench are installed.
- Run following command to spin up mysql server from project root folder where docker-compose.yml file is present.
```
$ docker compose up -d 
```
- To stop/start mysql server, you can use docker desktop or following commands.
```
$ docker compose stop
$ docker compose start
```
- Run database.sql file using MySQL Workbench to create tables for first time.


### References
