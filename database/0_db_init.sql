
-- Create databases and user for each database.

CREATE USER rating_uname WITH PASSWORD 'rating_uname_pwd';
CREATE DATABASE comments;
\c comments;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO rating_uname;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO rating_uname;

CREATE USER rest_uname WITH PASSWORD 'rest_uname_pwd';
CREATE DATABASE restaurants;
\c restaurants;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO rest_uname;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO rest_uname;

CREATE USER users_uname WITH PASSWORD 'users_uname_pwd';
CREATE DATABASE users;
\c users;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO users_uname;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO users_uname;
