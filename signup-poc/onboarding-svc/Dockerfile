FROM eclipse-temurin:22-alpine

EXPOSE 9000

VOLUME /tmp

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]