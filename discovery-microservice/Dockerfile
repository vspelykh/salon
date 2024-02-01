ARG AND_DOCKER_PROXY_REGISTRY_NAME
ARG BUILDER_IMAGE
ARG APP_IMAGE
FROM $AND_DOCKER_PROXY_REGISTRY_NAME/$BUILDER_IMAGE AS builder
ARG MAVEN_CLI_OPTS
WORKDIR /app
COPY discovery-microservice .
RUN mvn clean install $MAVEN_CLI_OPTS -Dmaven.test.skip

#app
FROM $AND_DOCKER_PROXY_REGISTRY_NAME/$APP_IMAGE
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-Xms128m", "-Xmx512m", "-jar", "/app/app.jar"]