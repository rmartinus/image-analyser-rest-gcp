# rest-gcp
Google Cloud Platform using Spring Cloud GCP

### Setup
* Set up Google Cloud Platform
* Create a project
* Install gcloud and login: `$ gcloud auth login`
* Set up GOOGLE_APPLICATION_CREDENTIALS
* To run it locally, run: `mvn clean package spring-boot:run`

### Running it in Google Cloud Platform
* Initialise app engine: `$ gcloud app create`
* Run `$ mvn clean package appengine:deploy`
* You should be able to access it in appspot, like: `https://single-router-216019.appspot.com/hello/world`

### Endpoints
* `$ curl http://localhost:8080/hello/world`
* `$ curl -F 'image=@Golden_Retrievers_dark_and_light.jpg' http://localhost:8080/analyse`
