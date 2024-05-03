# MicroService Template

This repo is a SpringBoot MicroService template to with some built-in tools. The idea is to be possible to fork it and start development without having to mind too much with enviroment configuration.

It contains several branches, the master contains the basis to every SpringBoot template, and each other contain an aditional tool. The bellow list contain all integrated tools and which branch they are in. The idea is so you can fork it and merge any branch that contain a necessary tool to your project.

## Tools
- Spring Web: make it easy to build HTTP APIs
- [Lombok](https://projectlombok.org/): add facilitators annotation to make code faster and cleaner
- [Checkstyles](https://checkstyle.sourceforge.io/): add code styles check to enforce code standards
    - Using [Google Java Style](https://google.github.io/styleguide/javaguide.html)
- [PMD](https://pmd.github.io/): code analyser to avoid code mistakes
- PostgreSQL and Spring JPA: build and manipulate relational databases within Java code. Stored in [postgresql branch](https://github.com/gfmota/template/tree/postgresql)
  - To use it, it is necessary to add the following environment variables:
    ```
    DB_URL=
    DB_NAME=
    DB_USER=
    DB_PASS=
    ```
    - It also add docker compose to build postgresql container with the environment values
- [RabbitMQ](https://www.rabbitmq.com/): message broker handler. Stored in [rabbitmq branch](https://github.com/gfmota/template/tree/rabbitmq)
  - To use it, it is necessary to add the following environment variables:
    ```
    RABBITMQ_HOST=
    RABBITMQ_PORT=
    RABBITMQ_USER=
    RABBITMQ_PASS=
    ```
  - It also add docker compose to build postgresql container with the environment values

It also contains GitHub Actions already configured to build Gradle on pull request creation.