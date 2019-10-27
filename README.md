# UMF
[![Build Status](https://travis-ci.org/sothach/umf.svg?branch=master)](https://travis-ci.org/sothach/umf)
[![Coverage Status](https://coveralls.io/repos/github/sothach/umf/badge.svg?branch=master&kill_cache=1)](https://coveralls.io/github/sothach/umf?branch=master&service=github)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7fd846368a8e4d539a0154f8ff5a1af4)](https://www.codacy.com/manual/sothach/umf?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sothach/umf&amp;utm_campaign=Badge_Grade)

## Task
Create a simple command line JAVA application with database access;

Based on an input from the command line provide the following functionality;

Provide help

1.  Add Person (id, firstName, surname)
2.  Edit Person (firstName, surname)
3.  Delete Person (id)
4.  Count Number of Persons
5.  List Persons

Additional Requirements (nice to have, but not required);

1.  Ability to Add Person from XML (Senior Only)
2.  Test coverage
3.  Maven or Gradle Build
4.  Executable Jar

Show us what you know, use any third party frameworks that you think will help you complete the exercise. Keep it simple.

Please supply the source code and instructions on how to build and run your application.

Please upload your all or your code & work into Github, Dropbox or Drive etc... and send us the link.

Please spend no more than 4 hrs on this.

## Assumptions
*  Person records are unique based on firstname, surname (case insensitive)
*  Volumes will be low (not using reactive patterns, e.g., for file loading)
*  i18n is not a requirement (English text only will be used in interface)

## Design decisions
*  Using Spring-boot CommandLineRunner as application framework
*  Storage is Mongodb: simple schema-less object store, good Spring support, embedded version facilitates testing
*  Fasterxml XML parsing library used: contaminates domain object, but considering as person interface context model in 
this simple application

## Build me
`% mvn clean package spring-boot:repackage`

## Set-up
if installed locally, start daemon:

`% mongod`

or run in docker:

`% docker run -d -p 27017:27017 -v ~/data:/data/db mongo1`

## Run me
`% java -jar target/umf-0.0.1-SNAPSHOT.jar`

## Query Database
`% mongo`
```bash
> use manifest
switched to db manifest
> db.person.find( {} )
{ "_id" : "10001", "firstName" : "otto", "surname" : "normalverbraucher", "_class" : "org.anized.umf.model.Person" }
{ "_id" : "10002", "firstName" : "hans", "surname" : "mustermann", "_class" : "org.anized.umf.model.Person" }
{ "_id" : "10004", "firstName" : "ivor", "surname" : "driver", "_class" : "org.anized.umf.model.Person" }
{ "_id" : "1234", "firstName" : "freddy", "surname" : "finkelstein", "_class" : "org.anized.umf.model.Person" }
```

## Build Docker image
`% mvn install dockerfile:build`

## Run Docker locally
`% docker run --interactive sothach/umf`
