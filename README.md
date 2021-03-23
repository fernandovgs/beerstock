# Beerstock
Practice project for Inter Java Developer Bootcamp.

## Introduction

Beerstock is a project developed during 
[Inter Java Developer Bootcamp, at Digital Innovation One](https://digitalinnovation.one/bootcamps/inter-java-developer). 
The idea of this project is to apply TDD concepts in a Spring Boot API that handles a fictional stock of beers with simple CRUDs.

This README is a resume of activities made and concepts trained during the course "Desenvolvimento de testes unitários para validar uma API REST de gerenciamento de estoques de cerveja" (Unit test development for a REST API that manages beer stocks).

## TDD - Test Driven Development
TDD is a technique of developing software by creating unit tests for application methods before the implementation of these methods themselves. Then, we implement code to pass the created tests.

The main advantages of TDD is:
* Cleaner code;
* Prevent bugs (it does not make your code immune to them, though!);
* Quick feedback when implementing code.

## Tools used

1. **Spring Boot**: Spring project that makes significantly easier to start Spring projects by simply adding starters inside pom.xml.
2. **Intellij Community Edition**:  [Jetbrains](https://www.jetbrains.com/pt-br/idea/) IDE focused on languages that run in JVM (mainly Java, of course).
3. **Postman**: Development app that tests API endpoints.
4. **Spring Initializr**: a [website](https://start.spring.io/) that automatically generates a base Spring Boot project, based on the configs passed to it.

## About the development process

I made this project using the teacher videos as a reference. I structured the packages based on my brief experience with Spring Boot and based on the teacher's explanations along the videos.
However, for the implementation of the functionalities, I created the tests first, trying to approach in the TDD processes.
The development process was also based on [Gitflow Workflow](https://www.atlassian.com/br/git/tutorials/comparing-workflows/gitflow-workflow), which, basically consists on creating a complementary 
branch called develop, then implementing each feature in new branches, merging those "feature branches" into develop, and finally merging develop into main.
The advantage here is to separate versions from each feature more easily and ensure more stability among official versions, and of course, I ended learning more about git commands. It was good and fun to study!

I also tried to use maximum of knowledge gained from the main project by adding my own functionalities to the project.
Basically, it consisted in creating an entity called "Shopkeeper", which is a fictional actor from our project that can distribute the beers from the stock.
This entity, then, has an N to N relationship with Beer entity, since a Shopkeeper can distribute one or more beers from our stock,
and each beer can have several distributors.

Finally, I tried to apply some knowledge from previous courses from the bootcamp, like creating two separate environments: test and dev.
Dev is linked to a [PostgreSQL](https://www.postgresql.org/) instance, while Test uses the in-memory database called [H2](https://www.h2database.com/html/main.html).

## Project Structure

* **main**
    * main
        * main package (one.digitalinovvation.beerstock).
            * controllers: handles REST endpoints.
                * docs: interfaces that documents the REST endpoints using Swagger annotations.
            * domains: handles all data modelling, such as database entities, such as:
                * dtos: represents DATA TRANSFER OBJECTS from the system.
                * entities: represents entities from database.
                * enums: represents enum data types used by entities.
                * mappers: interfaces that maps methods to create an entity object from a DTO, and vice versa.
                * repositories: interfaces for JPA repositories.
            * services: handles the business logic of the project.
            * constants: I tried to separate some commonly used strings into constants on this package.
            * infrastructure: responsible for infrastructural classes, such as:
                * configs: configuration beans for Spring. Currently, only using a SwaggerConfig class.
                * exceptions: stores all Exception classes to throw when an error occurs.
        * resources: stores .properties files. Helps to configure the environments.
    * test: handles project's unit tests and other useful classes.
        * builder: has proper DTOS for tests. It uses Builder pattern.
        * controllers: has unit tests for controller methods.
        * services: has unit tests for service methods.
        * utils: useful methods that helps us to make unit tests. Currently, it only has JsonConversionUtils, responsible for converting a Java Object to a JSON string. 
        