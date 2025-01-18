# Simple REST API CTF
## Description
This is the first CTF challenge I'm providing in this style. It should be a simple one. Review the code, exploit the flaws, and read the flag from the protected mapping. There are a couple paths to victory, but do disregard the data that is plainly available in the .env and SampleData files.
## Requirements
* docker desktop
* java 17
* bridged connection (assuming vm)
## Concessions
* TLS
* DoS
* rate limiting
* CORS
* logging and monitoring
* password policy
* the contents of the .env and SampleData files
## Quick Guide
* set WORKDIR in the .env file
* fire up
    ```bash
    sudo docker compose up
    ```
* power down and clean (keeps mongo image)
    ```bash
    sudo docker compose down; sudo docker image rm notekeeper:1.0.0; sudo docker volume rm ctf-01_mongo; sudo docker volume prune
    ```
* purge docker resources to reclaim more space - DANGEROUS
    ```bash
    sudo docker system prune --all --volumes
    ```
