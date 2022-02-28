FROM openjdk:11-jre-slim
ADD jars/where-money-api.jar where-money-api.jar
ENTRYPOINT ["java", "-jar","where-money-api.jar"]