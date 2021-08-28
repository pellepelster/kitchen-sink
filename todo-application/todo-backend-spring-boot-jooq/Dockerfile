FROM openjdk:14-alpine

ARG JAR_FILE=todo-backend-spring-boot-jooq.jar

ENV USER=todo

CMD mkdir -p /todo-backend-spring-boot-jooq/app

RUN addgroup -S ${USER} && adduser -S -G ${USER} ${USER}
USER ${USER}

EXPOSE 8080

COPY ${JAR_FILE} /todo-backend-spring-boot-jooq/app/todo-backend-spring-boot-jooq.jar
WORKDIR /todo-backend-spring-boot-jooq/app/

CMD java $JAVA_OPTIONS -Dspring.profiles.active=prod -jar todo-backend-spring-boot-jooq.jar
