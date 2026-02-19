compose-up:
	docker-compose -f ./docker-compose.yaml up -d

compose-down:
	docker-compose -f ./docker-compose.yaml down -v

test:
	./gradlew test

run:
	./gradlew bootRun