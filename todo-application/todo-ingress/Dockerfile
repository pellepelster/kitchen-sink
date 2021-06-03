FROM alpine:3.7

RUN apk add --no-cache nginx

CMD mkdir -p /todo-ingress/config
COPY config/ /todo-ingress/config

ENTRYPOINT ["nginx"]
CMD ["-g", "pid /tmp/nginx.pid;", "-c", "/todo-ingress/config/nginx.conf"]
