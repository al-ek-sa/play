FROM eclipse-temurin:25-jre
WORKDIR /app
COPY build/libs/untitled-1.0-SNAPSHOT.jar app.jar
EXPOSE 6060
CMD ["java", "-DFCGI_PORT=6060", "-jar", "app.jar"]