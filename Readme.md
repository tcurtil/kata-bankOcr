# BankOCR

From http://codingdojo.org/kata/BankOCR/

## Prerequisites

- at least a jvm 8
- maven 3

## Setup

to prepare for eclipse import, you may run this command :

    mvn eclipse:eclipse

## Running tests :

via eclipse or maven :

    mvn test
    
## Producing the runnable application

    mvn package
    
## Running the application

    java -jar target/bank-ocr-1.0.0.jar --dirpath={path}

or :

    mvn clean spring-boot:run -D"--dirpath={path}"

