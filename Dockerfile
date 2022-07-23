FROM amazoncorretto:11-alpine3.13
RUN mkdir /wb
WORKDIR /wb
COPY build/quarkus-app .

EXPOSE 1912

CMD ["java", "-jar", "quarkus-run.jar"]

