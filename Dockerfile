FROM maven:3.8.5-openjdk-17
COPY pom.xml /app/
COPY src /app/src
WORKDIR /app
RUN mvn package

FROM openjdk:17
COPY --from=0 /app/target/ts3status-1.0-SNAPSHOT.jar /app/ts3status-1.0-SNAPSHOT.jar
CMD java -jar /app/ts3status-1.0-SNAPSHOT.jar
