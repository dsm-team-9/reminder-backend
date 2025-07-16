FROM azul/zulu-openjdk:17-latest
WORKDIR /app
COPY build/libs/reminder-be-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]