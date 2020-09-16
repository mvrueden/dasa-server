FROM adoptopenjdk/openjdk11:alpine

ARG JAR_FILE
COPY target/${JAR_FILE} /opt/app.jar

RUN apk add --no-cache bash
RUN apk add --no-cache curl

HEALTHCHECK --interval=5s --timeout=5s --retries=10 \
  CMD curl --silent --fail http://localhost:8080/health || exit 1

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]