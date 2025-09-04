FROM amd64/maven:3.9-eclipse-temurin-24 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:24.0.2_12-jdk
COPY --from=build target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]


