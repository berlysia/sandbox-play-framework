#!/bin/bash
docker run --name postgres-container -e POSTGRES_PASSWORD=mysecretpassword -d postgres:9.6.3
# PORT 5432
