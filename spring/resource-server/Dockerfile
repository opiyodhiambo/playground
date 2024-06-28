FROM eclipse-temurin:22-alpine

EXPOSE 9090

VOLUME /tmp

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]