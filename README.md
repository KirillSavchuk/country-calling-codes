# Country Calling Codes application

## Initial Task

### Functional requirements

Write a microservice to determine the country by phone number.

The user enters the phone number, the system validates it and shows the country or error message.

For country codes, use the table on the page: https://en.wikipedia.org/wiki/List_of_country_calling_codes

(You must download data from it every time you start the service.)

### Non-functional requirements

### Backend

* Java 8+
* Spring Boot
* Spring Data JPA – MySQL
* Maven/Gradle
* HTTP, RESTful service with JSON data format.

### Frontend

* HTML
* JavaScript
* CSS

### Comments

* The application must be assembled and launched from the command line, on port 8080.
* It should also be possible to run tests and view reports on them.
* All calls to the application are made through the RESTful service with JSON as the data format.
* The appearance of the interface is unimportant, enough neat HTML.
* For queries, use any AJAX-capable framework, you can just JQuery.
* Data validation, tests are mandatory.

## How to run

### Tests

In order to run backend tests and view test report, execute `./create-test-report.sh` in `backend` directory.

HTML test report will be generated here:

```
/backend/target/site/surefire-report.html
```

### Preparation

Before running all application in Docker, build backed in `backend` directory:

```bash
mvn clean package
```

### Docker

All Docker scripts are stored in `docker` directory.

In order to run application, just execute script from `run.sh` file.

Other Docker scripts are explained below:

| Script          | Description                                                             |
|-----------------|-------------------------------------------------------------------------|
| run.sh          | Runs whole application, including `database`, `backend` and `frontend`  |
| run.database.sh | Creates MySQL database instance with `root:root` user under `3036` port |
| run.backend.sh  | Runs or recreates backend application under `8081` port                 |
| run.frontend.sh | Runs or recreates fronted application under `8080` port                 |
| clean.sh        | Cleanups all containers and volumes from `docker-compose.yml`           |

### URLs

* Swagger UI: http://localhost:8081/swagger-ui/index.html
* List of country calling codes: http://localhost:8080/country-calling-codes
* Phone Number Info: http://localhost:8080/phone-number-info

## Future work

### Code improvements

#### FrontEnd

* Add RegEx validation for phone number in submit form.

#### BackEnd

* Move `lv/savchuk/country/calling/codes/wiki` package to `country-calling-codes-wiki-provider` module.
* Setup Allure test report.