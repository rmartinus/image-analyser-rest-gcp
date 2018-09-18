# image-analyser-rest-gcp
Image analyser restful service using Spring Cloud GCP and Google App Engine

### Setup
* Set up Google Cloud Platform
* Create a project
* Install gcloud and login: `$ gcloud auth login`
* Set up GOOGLE_APPLICATION_CREDENTIALS
* To run it locally, run: `$ mvn clean package spring-boot:run`

### Running it in Google Cloud Platform
* Choose the right project, eg. `$ gcloud config set project my-image-analyser`
* Initialise app engine: `$ gcloud app create`
* Run `$ mvn clean package appengine:deploy`
* You should be able to access it in appspot, like: `https://my-image-analyser.appspot.com/actuator/health`

### Endpoints
* `$ curl -F 'image=@Golden_Retrievers_dark_and_light.jpg' http://localhost:8080/analyse`
