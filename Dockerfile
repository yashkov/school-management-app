FROM gradle:8.14.3-jdk21 AS builder

WORKDIR /workspace

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew

COPY src src

# Build executable Spring Boot JAR.
RUN ./gradlew --no-daemon clean bootJar -x test
RUN cp "$(ls build/libs/*.jar | head -n 1)" /workspace/app.jar


FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN useradd --system --uid 10001 appuser

COPY --from=builder /workspace/app.jar /app/app.jar

USER appuser

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
