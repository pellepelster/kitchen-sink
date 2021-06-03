FROM alpine:3.7

RUN apk add --no-cache nginx

CMD mkdir -p /todo-frontend-angular/www
CMD mkdir -p /todo-frontend-angular/config
CMD mkdir -p /todo-frontend-angular/run

COPY dist/todo-frontend-angular/ /todo-frontend-angular/www
COPY config/ /todo-frontend-angular/config

ENTRYPOINT ["nginx"]
CMD ["-g", "pid /tmp/nginx.pid;", "-c", "/todo-frontend-angular/config/nginx.conf"]
