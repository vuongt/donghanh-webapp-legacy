# Dong Hanh web app for candidate evaluation
## Set up local development
Requirements: 
* Docker
* Java 11

1. Start mysql service: 
`docker-compose up`

2. Start webapp server:
`./gradlew appRun`

## Deploy on server

1. Generate .war file, the file is output to build/libs/DH.war
`./gradlew war`

2. Deploy DH.war to tomcat server