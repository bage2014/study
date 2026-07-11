FROM openjdk:21-jre-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8081"]
