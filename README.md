# geolocation
Service to find geolocation of countries
- The application contains both the logic to load the data csv to database
- The start the webservice for accessing the endpoint.
- Currently it utilized in memory h2 db, and the webservice only starts up after all data has been loaded.

## Build:
- `sbt clean assembly`
- `docker build . -t geolocation:1.0.0`

## To Run use

- `docker run -p 8083:8083 -it geolocation:1.0.0`
-  use `-e FILE_PATH=${path of data_dump.csv}` file.

## The api can be accesset via endpoints:

*GET*
- `api/locations`
- `api/locations/${ipAddress}`

The get all endpoiint has default pagination implemented with pagesize of 16 records.
For custom pagination use:

GET
- `api/locations?pageSize=${size}&&pageNumber=number`

## Before startup
- the application loads the data.csv file which contains the geolocation.
