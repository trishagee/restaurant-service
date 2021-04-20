FROM openjdk:16-jdk-alpine
COPY target/*.jar restaurant-service.jar
ENTRYPOINT ["java", "-jar", "/restaurant-service.jar"]