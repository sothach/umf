# UMF
## Task
Create a simple command line JAVA application with database access;

Based on an input from the command line provide the following functionality;

Provide help

1. Add Person (id, firstName, surname)
2. Edit Person (firstName, surname)
3. Delete Person (id)
4. Count Number of Persons
5. List Persons

Additional Requirements (nice to have, but not required);

1. Ability to Add Person from XML (Senior Only)
2. Test coverage
3. Maven or Gradle Build
4. Executable Jar

Show us what you know, use any third party frameworks that you think will help you complete the exercise. Keep it simple.

Please supply the source code and instructions on how to build and run your application.

Please upload your all or your code & work into Github, Dropbox or Drive etc... and send us the link.

Please spend no more than 4 hrs on this.

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
> db.user.find( {} )
{ "_id" : ObjectId("5db2aa9a67ac350f66fca7b8"), "firstName" : "fred", "surname" : "Flintstone", "_class" : "org.anized.umf.model.User" }
```

## Build Docker image
`% mvn install dockerfile:build`

## Run Docker locally
`% docker run --interactive sothach/umf`