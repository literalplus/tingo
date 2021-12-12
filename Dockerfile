FROM eclipse-temurin:17-jdk-alpine as jre-build

# Create a custom Java runtime
# https://github.com/adoptium/containers/issues/80
# java.desktop: For java/beans/PropertyEditorSupport -> Spring-Context
# java.logging: For java/util/logging/Logger -> org.yaml.snakeyaml.TypeDescription
# java.sql: For java/sql/DataSource -> database
# java.naming: For javax.naming.NamingException -> DataSourceBuilder$HikariDataSourceProperties
# java.management: For javax.management.NotificationEmitter
# java.instrument: For java.lang.instrument.ClassFileTransformer -> PersistenceUnit something
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base \
         --add-modules java.desktop \
         --add-modules java.logging \
         --add-modules java.sql \
         --add-modules java.naming \
         --add-modules java.management \
         --add-modules java.instrument \
         --strip-java-debug-attributes \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

FROM gradle:7.2-jdk17 AS build

COPY . .

RUN gradle build

FROM alpine:3 AS run
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME
COPY --from=build /home/gradle/build/libs/tingo.jar /tingo.jar

EXPOSE 8080
ENTRYPOINT ["/opt/java/openjdk/bin/java", "-jar"]
CMD ["-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=80.0", "/tingo.jar"]
