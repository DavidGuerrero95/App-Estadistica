FROM openjdk:12
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Estadistica.jar
ENTRYPOINT ["java","-jar","/Estadistica.jar"]