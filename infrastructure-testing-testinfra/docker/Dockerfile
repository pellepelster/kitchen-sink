FROM alpine:3.19.1

RUN apk --no-cache add caddy

RUN addgroup \
    --gid "10000" \
    "www" \
&&  adduser \
    --disabled-password \
    --gecos "" \
    --home "/www/public_html" \
    --ingroup www \
    --uid 10000 \
    "www"

RUN mkdir -p /www/public_html
ADD index.html /www/public_html/
RUN chown www:www /www/public_html
ADD Caddyfile /www/Caddyfile
USER www
CMD ["caddy", "run", "--config", "/www/Caddyfile"]
