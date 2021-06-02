FROM openjdk:12-alpine

ENV USER=todo-application

RUN addgroup -S ${USER} && adduser -S -G ${USER} ${USER}
USER ${USER}

EXPOSE 8080

COPY build/libs/todo-application.jar ./todo-application.jar
CMD java $JAVA_OPTIONS -Dspring.profiles.active=prod -jar ./todo-application.jar
