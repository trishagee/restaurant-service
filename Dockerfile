FROM eclipse-temurin:25-alpine
COPY target/*.jar restaurant-service.jar
ENTRYPOINT ["java", "-jar", "/restaurant-service.jar"]