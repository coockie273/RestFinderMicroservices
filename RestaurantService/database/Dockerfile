FROM postgres:latest

COPY restaurants.sql /docker-entrypoint-initdb.d/

VOLUME /var/lib/postgresql/data