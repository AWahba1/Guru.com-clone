FROM postgres:latest

# Copy initialization scripts into the container
COPY ./database/SQL/Jobs /docker-entrypoint-initdb.d/