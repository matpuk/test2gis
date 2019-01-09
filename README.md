# Simple Booking Service

This is a simple REST service to book seats in a cinema hall. It is written in Java and uses the following technologies:

- Jetty (Web Server and Servlet Container)
- Jersey (RESTful Web Services Framework)
- HK2 (Dependency Injection Framework)
- Hibernate (ORM)
- H2 (SQL database)

## Installation

### Prerequisites

Oracle JDK or OpenJDK version 1.8 or later and Maven version 3.5.0 or later.

### Build from sources

Clone source code from GitHub

    # git clone https://github.com/matpuk/test2gis.git

Change your current work directory to the service source root

    # cd test2gis

Compile the service and run tests

    # mvn test

Build the service package

    # mvn package

The service package will be created in the `target` folder and named like `cinema-booking-service-x.x.x-SNAPSHOT.jar`.

### Deployment

1. Upload the service package `cinema-booking-service-x.x.x-SNAPSHOT.jar` to any host with Oracle JRE or OpenJDK JRE version 1.8 or later installed.
2. Copy sample H2 database `db/bookingdb.mv.db.sample` to the same folder where the package reside.
3. Rename the database file to `bookingdb.mv.db`

## Usage

### Running the service

To start the service, just issue the following command (make sure that your current work directory is the one with the package)

    # java -jar ./cinema-booking-service-x.x.x-SNAPSHOT.jar

By default, the service will listen on port 8080. You can change it by defining `TEST2GIS_PORT` env var

    # env TEST2GIS_PORT=8181 java -jar ./cinema-booking-service-x.x.x-SNAPSHOT.jar

### REST API

The service API is available under `/api/v1` prefix and can be accessed via HTTP protocol.

The service provides the following REST API:

**/halls**

> Method: GET
>
> Description: Get a list of all cinema halls.
>
> Success Response:
>
> - Code: 200
> - Content: [ {"id": 1, "name": "Big Hall", "numSeats": 15}, ...]
>
> Example:
>
>     # curl http://<hostname>:8080/api/v1/halls

**/halls/{hallId}**

> Method: GET
>
> Description: Get information about specific cinema hall.
>
> Success Response:
>
> - Code: 200
> - Content: {"id": 1, "name": "Big Hall", "numSeats": 15}
>
> Error Response:
>
> - Code: 404
> - Content: {"status": 404, "message": "Hall not found: 111"}
>
> Example:
>
>     # curl http://<hostname>:8080/api/v1/halls/1

**/halls/{hallId}/seats**

> Method: GET
>
> Description: Get information about seats of specific hall.
>
> Success Response:
>
> - Code: 200
> - Content: [ {"id": 1, "hallId": 1, "booked": false}, ...]
>
> Error Response:
>
> - Code: 404
> - Content: {"status": 404, "message": "Hall not found: 123"}
>
> Example:
>
>     # curl http://<hostname>:8080/api/v1/halls/1/seats

**/halls/{hallId}/bookSeats**

> Method: POST
>
> Description: Book seats in specific cinema hall.
>
> Data Params:
>
> List of seat IDs to book
>
> - Content: [4, 5, 6, ...]
>
> Success Response:
>
> - Code: 200
> - Content: None
>
> Error Response:
>
> - Code: 400
> - Content: {"status": 400, "message": "Invalid seat ID: 25"}
>
> - Code: 404
> - Content: {"status": 404, "message": "Hall not found: 111"}
>
> - Code: 409
> - Content: {"status": 409, "message": "At least one requested seat is already booked"}
>
> Example:
>
>     # curl -H "Content-Type: application/json" -X POST \
>         --data '[1,2]' http://<hostname>:8080/api/v1/halls/1/bookSeats

## License

MIT (See 'LICENSE' file for exact license text).

## Author

Grigory V. Kareev

Email: <grigory.kareev@gmail.com>

GitHub: [https://github.com/matpuk](https://github.com/matpuk)
