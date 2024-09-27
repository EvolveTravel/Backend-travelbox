FROM openjdk:22-jdk
VOLUME /tmp
EXPOSE 8080
COPY target/Backend-travelbox-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
