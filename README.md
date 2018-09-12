# rest-gcp
Google Cloud Platform using Spring Cloud GCP

### Setup
* Set up Google Cloud Platform
* Create a project
* Install gcloud and login
* To run it locally, run: `mvn clean package spring-boot:run`
* To run it using appengine, run `mvn clean package appengine:run`
* To deploy it to GCP, run `mvn clean package appengine:deploy`

### Endpoints
* `$ curl http://localhost:8080/hello/world`
* `$ curl http://localhost:8080/hello/world`
* `$ curl -F 'image=@Golden_Retrievers_dark_and_light.jpg' http://localhost:8080/analyse`
