FROM openjdk:22-slim

COPY target/kafka-consumer-postgres.jar app.jar

CMD ["java", "-jar", "app.jar"]