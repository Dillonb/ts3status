FROM maven:3-jdk-10
COPY pom.xml /app/
COPY src /app/src
WORKDIR /app
RUN mvn install
CMD mvn spring-boot:run
