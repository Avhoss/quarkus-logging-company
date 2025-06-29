# quarkus-logging-company
This is a showcase project demonstrating how to implement a Kafka-based logging system using **Quarkus 3.2.4** with **MSSQL** as the relational database. It includes producers and consumers for logging changes to entities like `Employee` and `Company`, which are persisted and tracked using `ChangeLog`.


## Features

- RESTful CRUD APIs for `Employee` and `Company`
- Kafka producer to send change logs
- Kafka consumer to persist logs to `ChangeLog` table
- MSSQL database integration using Hibernate ORM
- Panache-based entities for simplicity
- Docker Compose for Kafka & Zookeeper setup

---

## Tech Stack

- **Quarkus 3.2.4**
- **Apache Kafka**
- **MSSQL Server**
- **Panache ORM (Hibernate)**
- **SmallRye Kafka**

## Kafka and Zookeeper
Have docker ready in your local and use the following configuration of docker-compose
<details> <summary>📄 Click to view docker-compose.yml</summary>
version: '2'

services:

  zookeeper:
    image: strimzi/kafka:0.20.1-kafka-2.6.0
    command: [
        "sh", "-c",
        "bin/zookeeper-server-start.sh config/zookeeper.properties"
    ]
    ports:
      - "2181:2181"
    environment:
      LOG_DIR: /tmp/logs

  kafka:
    image: strimzi/kafka:0.20.1-kafka-2.6.0
    command: [
        "sh", "-c",
        "bin/kafka-server-start.sh config/server.properties --override listeners=$${KAFKA_LISTENERS} --override advertised.listeners=$${KAFKA_ADVERTISED_LISTENERS} --override zookeeper.connect=$${KAFKA_ZOOKEEPER_CONNECT}"
    ]
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      LOG_DIR: "/tmp/logs"
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
</details>
Then run with [docker-compose up -d] command

## SQL Command Setup
<details>
  
-- [1a] Create Database</br>
CREATE DATABASE Company_Logging;

-- [1b] use database</br>
USE Company_Logging

-- [2] Create Companies table</br>
CREATE TABLE Company (
	company_name VARCHAR(150) PRIMARY KEY,
	sector VARCHAR (50),
	location VARCHAR (150)
)

-- [3] Create Employees table</br>
CREATE Table Employee (
	id INT PRIMARY KEY IDENTITY(1, 1),
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(150),
	workplace_name VARCHAR(150) FOREIGN KEY REFERENCES Company(company_name)
)

-- [4] Create ChangeLog table</br>
CREATE TABLE ChangeLog (
	id INT IDENTITY(1, 1) PRIMARY KEY,
	change_type VARCHAR(100) NOT NULL,
	description VARCHAR(1000) NOT NULL,
	log_date DATETIME DEFAULT GETDATE()
)

-- [5] Creating dummy data for company and employee
</br>
INSERT INTO Company(company_name, sector, location)
VALUES ('Company1', 'Financial', 'Surabaya'),
('Company2', 'Technology', 'DKI Jakarta'),
('Company3', 'Health', 'Manado')
</br>
INSERT INTO Employee(first_name, last_name, workplace_name)
VALUES('Reno', 'Aditya', 'company1'),
('John', 'Doe', 'company3'),
('Jane', 'Doe', 'company2')


</details>

## Starting
- After the docker for kafka and zookeeper is up and the DB is all setup -> Go to the project and run mvn quarkus:dev

## Testing the Kafka through API
You may try to update Employee from the POST request API: 
<a>http://localhost:8080/company-access/employee</a>
</br> 
with payload such as
</br> {</br> 
    "id": 1,</br> 
    "firstName": "Reno",</br> 
    "lastName": "Aditya",</br> 
    "workplaceName": "company3"</br> 
}
</br></br> </br> 
You may try to update Company from the POST request API:
http://localhost:8080/company-access/company</a>
with payload such as
</br> {</br> 
    "companyName": "company2",</br> 
    "sector": "Health",</br> 
    "location": "DKI Jakarta"</br> 
}
</br></br> </br> 
GET Request from API
<a>http://localhost:8080/company-access/change-log</a></br> 
To see the updates from the Log API of both company and employee

## Contact
Feel free to reach out if you're a recruiter or developer interested in this showcase:</br>
Email: mendel.jixk@gmail.com
