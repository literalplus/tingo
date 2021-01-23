FROM gradle:4.6-jdk8 AS build

COPY . .

RUN ./gradlew build

FROM adoptopenjdk/openjdk8:alpine-slim AS run
COPY --from=build /home/gradle/build/libs/tingo.jar /tingo.jar

EXPOSE 8080
ENTRYPOINT ["/opt/java/openjdk/bin/java", "-jar"]
CMD ["-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=80.0", "/tingo.jar"]
