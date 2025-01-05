FROM maven:3.9.7-eclipse-temurin-21 AS dependencies
WORKDIR /opt/app
COPY pom.xml .
RUN mvn -B -e dependency:go-offline

FROM maven:3.9.7-eclipse-temurin-21 AS builder
WORKDIR /opt/app
COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /opt/app/ /opt/app
COPY ./src /opt/app/src/
RUN mvn -B -e clean install -DskipTests

FROM eclipse-temurin:21.0.2_13-jre-jammy AS final
WORKDIR /opt/app
EXPOSE 8761
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]
