FROM postgres:latest

COPY restaurants.sql /docker-entrypoint-initdb.d/
COPY comments.sql /docker-entrypoint-initdb.d/
COPY users.sql /docker-entrypoint-initdb.d/

VOLUME /var/lib/postgresql/data
