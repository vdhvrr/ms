FROM adoptopenjdk/openjdk9:alpine
ARG JAR_FILE=target/*spring-boot.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]