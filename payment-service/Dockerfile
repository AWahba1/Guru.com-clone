FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ./target/PaymentMS-0.0.1-SNAPSHOT.jar paymentApp.jar
ENTRYPOINT ["java", "-jar", "paymentApp.jar"]