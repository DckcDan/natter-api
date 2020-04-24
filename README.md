# REST API Security project - The Natter API
This project aims to implement security controls to a REST API as described in the API Security in action book, it will also add some best practices for REST API design and exception handling.

The Natter-API is a social media API that allows the creation of spaces and the publishing of messages.

### Status
IN-PROGRESS

## Architecture
REST MVC application with an embedded Mongo DB.
Model layer which intends to server as a public model for this API.
Domain layer which intends to only be used internally to persist data in MongoDB or any non-sql store

This separation comes very important for two reasons:
- Separation of the external and internal model which will help the maintainability of this API.
- Only expose via Model layer the data that is required for the user and keep internal private data in the Domain layer.

## Exception handling
This project attempts to create a consistency way to handling exceptions and returning a clear error response with the details that matters.
Error messages and error codes have been externalize so that they are easier to maintain and to have a clear view of all the application errors codes.

## API Control
Input validation

## Security controls

### Rate-Limiting
It is used to prevent users overwhelming your API with requests, limiting denial of service threats.
### Encryption
It ensures that data is kept confidential when sent to or from the API and when stored on disk, preventing information disclosure. Modern encryption also prevents data being tampered with.
### Authentication
It makes sure that users are who they say they are, preventing spoofing. This is essential for accountability, but also a foundation for other security controls.
### Audit logging
Audit logging is the basis for accountability, to prevent repudiation threats.
### Access control - Authorization
Access control preserves confidentiality and integrity, preventing information disclosure, tampering and elevation of privilege attacks.


## Tech stack
Spring boot, Spring security
MongoDB
Gradle



