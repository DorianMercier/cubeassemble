set PGPASSWORD=postgres

psql -h localhost -U postgres -c "DROP DATABASE IF EXISTS cubeassemble"
psql -h localhost -U postgres -c "DROP USER IF EXISTS cubeassemble"

psql -h localhost -U postgres -c "CREATE USER cubeassemble WITH ENCRYPTED PASSWORD 'cubeassemble'"
psql -h localhost -U postgres -c "CREATE DATABASE cubeassemble"

set PGPASSWORD=cubeassemble

psql -h localhost -U cubeassemble -d cubeassemble -c "\i reset_database.sql"

set PGPASSWORD=