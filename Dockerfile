FROM eclipse-temurin:17-jdk-alpine
COPY target/medical-0.0.1-SNAPSHOT.jar medical-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/medical-0.0.1-SNAPSHOT.jar"]