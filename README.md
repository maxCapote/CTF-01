# Base Repo for REST API CTF
## Description
This is the base to build off of for future CTF challenges.
## Requirements
* docker desktop
* java 17
## Concessions
* TLS
* DoS
* rate limiting
* CORS
* logging and monitoring
* password policy
* the contents of the .env file
## Quick Guide
* set WORKDIR in the .env file
* fire up
    ```bash
    sudo docker compose up
    ```
* power down and clean
    ```bash
    sudo docker compose down; sudo docker image rm notekeeper:1.0.0; sudo docker volume rm notekeeper_mongo; sudo docker volume prune
    ```
* purge docker resources to reclaim more space - DANGEROUS
    ```bash
    sudo docker system prune --all --volumes
    ```