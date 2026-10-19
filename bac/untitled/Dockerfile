FROM eclipse-temurin:25-jre
WORKDIR /app
COPY build/libs/untitled-1.0-SNAPSHOT.jar app.jar
EXPOSE 7070
CMD ["java", "-DFCGI_PORT=7070", "-jar", "app.jar"]