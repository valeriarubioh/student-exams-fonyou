
# Student exams FONYOU

Studen Exams project. The backend uses Docker Compose to set up a MySQL database along with PHPMyAdmin for easy database management.

## Installation

Clone the repository:

```bash
git clone https://github.com/valeriarubioh/student-exams-fonyou.git
```
Java JDK 22 should be installed

#### Setting Up the Backend and Database

```bash
docker-compose up -d
```
PhpMyAdmin is accessible at:
http://localhost:8090

* Username: root
* Password: root

Important: Make sure you run the Docker container before running the Spring Boot application, as Flyway is used to delegate the creation of database tables.

#### Running Your Spring Boot Application

The application will be available at: http://localhost:8091
```
./mvnw spring-boot:run
```
## API
Open a browser and access http://localhost:8091/swagger-ui/index.html

The available endpoints are:
### Student Endpoints
- POST `/api/v1/students`: Send data to create a student.
### Exam Endpoints
- POST `/api/v1/exams`: Send data to create an exam.
### Exam Schedule Endpoints
- POST `/api/v1/exam-schedules`: Send data to assign an exam to a student, specifying a start and end time for the exam schedule. (The time zone is assumed to be America/Bogota).
- POST `/api/v1/exam-schedules/:examScheduleId/responses`: Send data for responding exam questions. (Use the query parameter examScheduleId to identify the exam schedule).
- GET `/api/v1/exam-schedules/:examScheduleId/grade`: Get the grade based on the student's exam responses. (Use the query parameter examScheduleId to identify the exam schedule).

Additionally:

Postman Collection: In the "postman" folder, you'll find a collection that you can import to easily test the endpoints.
Documentation Folder: Inside the "documentation" folder, you can learn the steps to test the collection effectively.

## Additional Resources

Docker Compose with MySQL and PhpMyAdmin: https://tecadmin.net/docker-compose-for-mysql-with-phpmyadmin/
