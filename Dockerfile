FROM openjdk:17

# DB
ARG DB_HOST
ARG DB_PORT
ARG DB_URI
ARG DB_NAME

# REDIS
ARG REDIS_HOST
ARG REDIS_PORT

# JWT
ARG JWT_SECRET
ARG JWT_VALIDITY_TIME
ARG JWT_REFRESH_VALIDITY_TIME

# API_KEY
ARG SEOUL_API_KEY
ARG SK_API_KEY
ARG SUBWAY_PATH_API_KEY

ENV DB_HOST=${DB_HOST}
ENV DB_PORT=${DB_PORT}
ENV DB_URI=${DB_URI}
ENV DB_NAME=${DB_NAME}
ENV REDIS_HOST=${REDIS_HOST}
ENV REDIS_PORT=${REDIS_PORT}
ENV JWT_SECRET=${JWT_SECRET}
ENV JWT_VALIDITY_TIME=${JWT_VALIDITY_TIME}
ENV JWT_REFRESH_VALIDITY_TIME=${JWT_REFRESH_VALIDITY_TIME}
ENV SEOUL_API_KEY=${SEOUL_API_KEY}
ENV SK_API_KEY=${SK_API_KEY}
ENV SUBWAY_PATH_API_KEY=${SUBWAY_PATH_API_KEY}

ENV TZ=Asia/Seoul

WORKDIR /app

COPY build/libs/*.jar msg-0.0.1.jar

COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

CMD /wait-for-it.sh ${DB_HOST}:${DB_PORT} --timeout=30 -- java -jar msg-0.0.1.jar