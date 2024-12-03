FROM amazoncorretto:21
ENV ENVIRONMENT=prd
ADD target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]