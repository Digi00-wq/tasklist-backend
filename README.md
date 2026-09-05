# Task list backen

## Docker test server

run:
`docker run --name tasklist-db \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=secret \
  -e POSTGRES_DB=tasklist \
  -p 5432:5432 \
  -d postgres:16`
