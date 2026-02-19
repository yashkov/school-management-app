### Intro
Small service prepared for this assignment is referred to as school-management-app

### Requirements
- Java 21
- Gradle
- Docker
- docker-compose

### Instructions
- `make compose-up` to start containers
- `make test` to run tests
- `make run` to run app
- `make compose-down` to teardown containers

http://localhost:8080/swagger-ui/index.html to access swagger
### Comments
Requirements from the second part of the assignment have been addressed by adding a simple case flow: assuming that POST /cases endpoint is exposed to applicants, while other endpoints are exposed to school administration staff, the applicants should be able to open their case and wait for approval or rejection by an authorized employee.
Making this flow more realistic, would require some form of authentication and authorization, to ensure e.g. that applicants can see only their own cases, etc. (out of scope for this exercise)

The concept of an open case would also work for more complicated long polling logic, though it might make sense to extract such logic into a separate service (school-management-app -> `CreateCaseCommand` -> some-kind-of-polling-service -> `CaseUpdateEvent` -> school-management-app -> updates visible to applicant / applicants notified)

To avoid additional dependencies and not balloon-up scope of this exercise, some functionalities have been omitted (e.g. detailed logging, locking and versioning of db records) while other contain functioning draft (e.g. exception handling might need to include some additional cases).
Code does not include full test coverage but provides a reasonable overview of preferred testing strategy.
Code is loosely structured in a DDD-adjacent manner (e.g. separation of layers is not as strict as a full-scope project should implement).