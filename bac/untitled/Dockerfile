FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean war --no-daemon


FROM quay.io/wildfly/wildfly:latest

COPY --from=build /app/build/libs/lab3.war \
    /opt/jboss/wildfly/standalone/deployments/ROOT.war

EXPOSE 8080