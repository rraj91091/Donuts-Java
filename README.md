# Donut Application

_Reference project for a modern Java Spring Boot application_

### Contents

- **Java**
    - Collections
    - Streams
- **Spring**
    - Spring Boot
    - Spring Data JPA & Hibernate
    - Spring Cache
    - Spring Kafka Operations
    - Spring Actuator
    - Spring Security
    - Spring AOP
- **Others**
    - PGP Encryption
    - Docker & Docker Compose

### In the pipeline

- File IO
- FeignClient
- Spring Cloud Circuit Breaker
- SSH & SFTP
- AWS S3 Integration
- TestContainers library
- Custom Logging
- Metrics
- Tracing (Spring Cloud Sleuth)
- Spring Batch

### Useful Commands

#### Docker

| Description                                 | Command                                   |
|---------------------------------------------|-------------------------------------------|
| Build docker image                          | `docker build --no-cache -t donuts-app .` |
| Bring up docker images                      | `docker compose up -d`                    |
| Bring down docker images and remove volumes | `docker compose down --volumes`           |

### PowerShell

- Rename Files in
  Directory: `Get-ChildItem "<directory>" -filter "<filename>" | Rename-Item -NewName {($_.name).Replace("<old>","<new>")}`
