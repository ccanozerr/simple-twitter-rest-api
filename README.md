# simpleTwitterRESTApiJava
Simple Twitter's REST Api.

Firt of all you need to create Maven project to run your server.

After that create your Tomcat server. I used Tomcat 8.5. 

Then start your server and use program like postman to send request the API's.

API List

1)GET /healthcheck
Health-check endpoint which returns “ok” to indicate the application is up

2)POST /messages
extracts hashtags from given message, stores message and returns structured message

3)DELETE /messages/:id
deletes message with given id. If message not exist or user is not owner of the message return error

4)GET /messages/:id
returns details of single message

5)GET /messages
filters messages with following query parameters and returns them with reverse chronological order
Query parameters:
“tag”: <string>, // optional if given messages should contain slug of given tag,

“username”: <string>, // optional if given messages should be created by given user

“page”: <int> // optional default is 1,
“count”: <int> // optional default is 10. Number of messages returned should not exceed count
