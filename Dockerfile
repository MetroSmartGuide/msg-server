FROM openjdk:17 AS builder

WORKDIR /app

COPY src/ /app/src/
COPY build.gradle.kts /app/build.gradle.kts
COPY settings.gradle.kts /app/settings.gradle.kts

RUN ./gradlew clean build -x test

FROM openjdk:17

ARG DB_HOST
ARG DB_PORT
ARG DB_URI
ARG DB_NAME
ARG CLIENT_ID
ARG CLIENT_SECRET
ARG JWT_SECRET
ARG JWT_VALIDITY_TIME
ARG SEOUL_API_KEY
ARG SK_API_KEY

ENV DB_HOST=${DB_HOST}
ENV DB_PORT=${DB_PORT}
ENV DB_URI=${DB_URI}
ENV DB_NAME=${DB_NAME}
ENV CLIENT_ID=${CLIENT_ID}
ENV CLIENT_SECRET=${CLIENT_SECRET}
ENV JWT_SECRET=${JWT_SECRET}
ENV JWT_VALIDITY_TIME=${JWT_VALIDITY_TIME}
ENV SEOUL_API_KEY=${SEOUL_API_KEY}
ENV SK_API_KEY=${SK_API_KEY}

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar msg-0.0.1.jar

COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

CMD /wait-for-it.sh localhost:${DB_PORT} --timeout=30 -- java -jar msg-0.0.1.jar