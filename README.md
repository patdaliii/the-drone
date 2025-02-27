#The Drone

A project I created for a Coding Assessment using Java Spring Boot and Maven as my build tool.

## Prerequisites
- Install Java version 17 or higher
- Install Maven 4.0 or higher

## Installation

Steps to set up the project locally
  - git clone https://github.com/patdaliii/the-drone.git
  - cd the-drone

Building the Project
  - mvn clean package (to package the project but not install in the local repository)
  - mvn clean install (to package and install in the local repository)
Note that either of these commands will automatically run the unit tests included in the project.

Running the Project
  - mvn spring-boot:run (this will run the tests prior to running the project)
  - mvn spring-boot:run -DskipTests (this will skip the tests and will just run the project)

Testing the Project
  - mvn test (if you want to run the tests without running the project)

Notes in testing the APIs
 - After loading the medication to the drone, there's approximately 20 secs before the Scheduler will revert the Drone's State to IDLE from LOADING. It would be best to test the API in checking the medications loaded in the drone within those 20 seconds to be able to get a Response Body that is not null.
