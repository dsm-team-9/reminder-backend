FROM azul/zulu-openjdk:17-latest
VOLUME /tmp
COPY build/libs/*.jar /app/
ENTRYPOINT ["java","-jar","/app.jar"]

