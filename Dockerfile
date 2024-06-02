FROM maven:3.8.5-openjdk-17
COPY pom.xml /app/
COPY src /app/src
WORKDIR /app
RUN mvn install
CMD mvn spring-boot:run
