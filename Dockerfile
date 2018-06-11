from maven:3-jdk-10
COPY . /app
WORKDIR /app
CMD mvn spring-boot:run
