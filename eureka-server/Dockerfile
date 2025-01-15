FROM maven:3.9.7-eclipse-temurin-21 AS dependencies
WORKDIR /opt/app
COPY pom.xml .
COPY config-server/pom.xml config-server/pom.xml
COPY entity/pom.xml entity/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY auth-service/pom.xml auth-service/pom.xml
COPY user-service/pom.xml user-service/pom.xml
RUN mvn -B -e -am -pl eureka-server dependency:go-offline

FROM maven:3.9.7-eclipse-temurin-21 AS builder
WORKDIR /opt/app
COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /opt/app/ /opt/app
COPY eureka-server/src /opt/app/eureka-server/src/
RUN mvn -B -e -am -pl eureka-server clean install -DskipTests

FROM eclipse-temurin:21.0.2_13-jre-jammy AS final
WORKDIR /opt/app
EXPOSE 8761
COPY --from=builder /opt/app/eureka-server/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]