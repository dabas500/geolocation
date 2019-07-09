# geolocation
Service to find geolocation of countries

## Build:
`docker build . -t geolocation:1.0.0`

## To Run use

- `docker run -p 8083:8083 -it geolocation:1.0.0`
- USE `-e FILE_PATH=${path of data_dump.csv}` file.

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
