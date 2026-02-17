# QA Automation (Java) - UI + API + DB

## Requirements
- Java 17+
- Maven 3.9+
- (Optional) Docker for DB services

## Run DB locally
docker compose up -d

## Run tests
mvn test

mvn -pl ui-tests -am test allure:serve

mvn -pl api-tests,ui-tests,db-tests -am test

## Allure
mvn allure:serve
