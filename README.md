# Challenge
this is the response for this [challenge](https://github.com/concretesolutions/desafio-java)

there's also a new version that I wrote in node js [here](https://github.com/rubentrancoso/challenge-node) with some simplifications.

In the root folder there's some helper scripts. 

The names are self-explanatory:
	
	build.sh
	buildimage.sh
	pushimage.sh
	run.sh
	runimage.sh
	stopimage.sh
	test.sh
	testimage.sh
	viewtree.sh

The application will run on `port 8080` by default but you can change whatever parameter you like on `application.yml`

Also in the root folder was left 3 exports from POSTMAN that were used to test the application.

    challenge-cloud.postman_environment.json
    challenge-local.postman_environment.json
    challenge.postman_collection.json
  
There's also unit tests and integration tests in the package itself.

The app is running on two addresses:

    https://concrete.doteva.com/hello
    http://mail.doteva.org:8081/hello

The urls follows the specification:

    POST - /register
    POST - /login
    GET  - /profile/{uuid}

## Curl samples
### Register
#### request
```
curl --request POST \
  --url https://concrete.doteva.com/register \
  --header 'content-type: application/json' \
  --data '{
    "name": "John Doe",
    "email": "johndoe@email.com",
    "password": "secret",
    "phones": [
        {
            "number": "987654321",
            "ddd": "21"
        }
    ]
	
}'
```
#### response
```
{
  "created": "Tue 2017-11-21 20:12:57.382+0000",
  "modified": "Tue 2017-11-21 20:12:57.382+0000",
  "id": "3c5b1cd17bcc41c5ab2081880fd931cc",
  "name": "John Doe",
  "email": "johndoe@email.com",
  "phones": [
    {
      "ddd": 21,
      "number": 987654321
    }
  ],
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huZG9lQGVtYWlsLmNvbSIsImV4cCI6MTUxMTI5Njk3N30.G7FIO5jYKbQq9NI5kseSlEdmKCMcoELL39gSCEA5xITF2_qOn4k8vw2cu5CVfAf3jH34QIYYMARl8lll4bpkLQ"
}
```
### Login
#### request
```
curl --request POST \
  --url https://concrete.doteva.com/login \
  --header 'content-type: application/json' \
  --data '{
    "username": "johndoe@email.com",
    "password": "secret"
}'
```
#### response
```
{
  "created": "Tue 2017-11-21 20:12:57.382+0000",
  "modified": "Tue 2017-11-21 20:14:33.739+0000",
  "id": "3c5b1cd17bcc41c5ab2081880fd931cc",
  "name": "John Doe",
  "email": "johndoe@email.com",
  "phones": [
    {
      "ddd": 21,
      "number": 987654321
    }
  ],
  "last_login": "Tue 2017-11-21 20:14:33.603+0000",
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huZG9lQGVtYWlsLmNvbSIsImV4cCI6MTUxMTI5NzA3M30.7Lu1spSpVCpmwoEbCMJiwPliGCD6nRg8m1Se9M6R136jZHYswI_ALn9X-2u3QCd03DtJKWTRXmyBAo3qCwtmOQ"
}
```
### Profile
#### request
```
curl -X GET \
  https://concrete.doteva.com/profile/3c5b1cd17bcc41c5ab2081880fd931cc \
  -H 'authorization: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huZG9lQGVtYWlsLmNvbSIsImV4cCI6MTUxMTI5NzA3M30.7Lu1spSpVCpmwoEbCMJiwPliGCD6nRg8m1Se9M6R136jZHYswI_ALn9X-2u3QCd03DtJKWTRXmyBAo3qCwtmOQ' \
  -H 'cache-control: no-cache'
```
#### response
```
{
  "created": "Tue 2017-11-21 20:12:57.382+0000",
  "modified": "Tue 2017-11-21 20:14:33.739+0000",
  "id": "3c5b1cd17bcc41c5ab2081880fd931cc",
  "name": "John Doe",
  "email": "johndoe@email.com",
  "phones": [
    {
      "ddd": 21,
      "number": 987654321
    }
  ],
  "last_login": "Tue 2017-11-21 20:14:33.603+0000"
}
```
### Development Notes
#### JVM starting to slow
in the case your jvm is getting much time to start take a look on your `/etc/hosts` file if it contains your actual hostname

extracted from [here](https://dzone.com/articles/fixing-the-slow-startup-time-of-my-java-applicatio)

```
127.0.0.1   localhost myhostname.doamin
::1         myhostname.doamin
```

#### Using spring devtools while in dev
spring-boot-devtools let you continuously run your application between changes
for that, on a terminal, run the `continuousbuild.sh` script and in the IDE start your app with `bootRun` as a Program Argument. 

extracted from [here](https://dzone.com/articles/continuous-auto-restart-with-spring-boot-devtools)

#### Auditing entities with created and modified fields
jpa can help to automatically update entities when they are creted and modified 

extracted from [here](https://programmingmitra.blogspot.com.br/2017/02/automatic-spring-data-jpa-auditing-saving-CreatedBy-createddate-lastmodifiedby-lastmodifieddate-automatically.html)

#### RestTesmplate Issue - java.net.HttpRetryException: cannot retry due to server authentication, in streaming mode
whenever RestTemplate receives a 401 status code it ignores ReponseBody

extracted from [here](https://stackoverflow.com/questions/27341604/exception-when-using-testresttemplate)

#### Map YAML lists to Objects

extracted from [here](https://www.fortisfio.com/yaml-file-mapping-values-to-object-list-with-spring-boot/)

#### JWT authentication


extracted from [here](https://dzone.com/articles/implementing-jwt-authentication-on-spring-boot-api)
