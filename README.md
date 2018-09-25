# image-analyser-rest-gcp
Image analyser restful service using Spring Cloud GCP and Google App Engine

### Setup
* Set up Google Cloud Platform
* Create a project
* Install gcloud and login: `$ gcloud auth login`
* Set up GOOGLE_APPLICATION_CREDENTIALS
* To run it locally, run: `$ ./mvnw clean package spring-boot:run`

### Running it in Google Cloud Platform
* Choose the right project, eg. `$ gcloud config set project my-image-analyser`
* Initialise app engine: `$ gcloud app create`
* Enable Cloud Vision API `$ gcloud services enable vision.googleapis.com`
* Enable Cloud SQL Admin API `$ gcloud services enable sqladmin.googleapis.com`
* Enable SQL API, provision SQL instance and a new db:
  - `$ gcloud sql instances create my-image-analyser --region=australia-southeast1`
  - `$ gcloud sql databases create my_image_analyser --instance my-image-analyser-2g`
* Double check application.properties has correct settings
* Run `$ ./mvnw clean package appengine:deploy`
* You should be able to access it in appspot, like: `https://my-image-analyser.appspot.com/actuator/health`

### Endpoints
* `$ curl -F 'image=@Golden_Retrievers_dark_and_light.jpg' http://localhost:8080/v1/analyse`

### Database proxy
* Run database proxy using cloud_sql_proxy tool provided by GCP and you an use DB tool like SequelPro to browse database to localhost:3306:
`$ ./cloud_sql_proxy -instances=my-image-analyser:australia-southeast1:my-image-analyser-2g=tcp:3306`
