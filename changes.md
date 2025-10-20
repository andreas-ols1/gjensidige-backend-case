# Changes to the Product API

## 1. Finish Product API

### `PUT` operation

Set ModelMapper config to skip null values to avoid updating the whole product, or setting illegal null values.

### `DELETE` operation

Implemented method in product controller and service that deletes a product and returns it in the response body.

## 2. Create Report Generation

Changed FinancialReport from model to entity so reports can be persisted in the database. To persist reports, a DB migration was run to create a new table. Implemented a controller and service that creates a report on a `POST` to the specified endpoint. Methods for getting one or many reports are also implemented using CRUD Repository. Reports are calculated using Java 8-style streams.

## Modernization

### Version bumps

1. Java: 8 -> 21
2. Spring Boot: 2.1.4 -> 3.5.6
3. JUnit: 4 -> 5
4. ModelMapper: 2.3.2 -> 3.2.5

### Replacements

Swagger Documentation -> OpenAPI (which includes Swagger)

- Spring Boot > v.3 does not support the old Swagger.

## Code Improvments

### Encapsulation

- Product and ProductDTO did not have private fields.
- Change Controller and Service to send DTO's instead of entities to have better encapsulation.

### Correct Use of HTTP Status Codes

- Implemented the use of `ResponseEntity` to give correct status codes in responses. Creating a product should return a response code of 201 Created.

### Controller testing

- Implemented MockMVC to accurately test controllers.

## Dockerization

Wrote a Dockerfile that runs the project in two stages, one for build and one for packaging. This minimizes the image size. The image can be built using this command:

```bash
cd api-product
docker build -t gjensidige-backend-case .
```

After building the image, it can be run with this command:

```bash
docker run -p 8080:8080 gjensidige-backend-case
```
