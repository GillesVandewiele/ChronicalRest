### ChronicalRest
## API documentation
All database calls are done by performing a GET or a POST request to the REST api
Possible queries are:
- Get a list of all drugs in the database
- Get a list of all symptoms in the database
- Get a list of all triggers in the database
- Get a patient in the database through firstName and lastName (*must be authenticated*)
- Store a new patient in the database

All calls go to the address: <<server ip>>/Chronic/rest/
This is the **base URL**.

