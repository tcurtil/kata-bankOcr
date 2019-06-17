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

    mvn clean test
    
This will produce a code coverage report at the same time.
You may consult it by opening target/site/jacoco/index.html in a browser.
    
## Producing the runnable application

    mvn clean package
    
## Running the application

    java -jar target/bank-ocr-1.0.0.jar [--dirpath={path}]

or :

    mvn clean spring-boot:run [-D"--dirpath={path}"]
    
The default dirpath is src/test/resources which contains files used during testing, and they cover the various test cases.

To run the analysis on the test case files, open http://127.0.0.1:8080/ while the application is running.
