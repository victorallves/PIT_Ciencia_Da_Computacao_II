FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/cupcakes-do-victor-0.0.1-SNAPSHOT.jar /app/cupcakes-do-victor.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/cupcakes-do-victor.jar"]
