FROM postgres:latest

COPY users.sql /docker-entrypoint-initdb.d/

VOLUME /var/lib/postgresql/data