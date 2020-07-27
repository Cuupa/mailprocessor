FROM openjdk:15-jdk
MAINTAINER Cuupa
WORKDIR /opt/app/mailprocessor
COPY *.jar / ./app.jar

ENTRYPOINT ["java", "-jar", "./app.jar"]