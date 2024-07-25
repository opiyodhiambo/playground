FROM eclipse-temurin:22-alpine

EXPOSE 8000

VOLUME /tmp

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]