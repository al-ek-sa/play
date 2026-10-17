FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY gradlew settings.gradle.kts build.gradle.kts fastcgi-lib.jar ./
COPY gradle gradle
COPY src src
RUN chmod +x gradlew && ./gradlew jar --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 7070
CMD ["java", "-DFCGI_PORT=7070", "-jar", "app.jar"]