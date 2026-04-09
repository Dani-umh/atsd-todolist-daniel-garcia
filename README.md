# To Do List app

ToDoList is a Spring Boot web application developed for the subject **Agile Techniques for Software Development**.  
The starting point was the initial project provided in the exercise, and the final delivered version of this application is **1.1.0**.

This version was developed following the professional workflow required in the assignment. Each feature was managed as a Trello user story and a GitHub issue, assigned to milestone **1.1.0**, developed in an independent feature branch, tested automatically using JUnit, reviewed through a pull request, merged into the `master` branch, and finally included in the GitHub release and Docker deployment workflow.

## Project boards

- Trello board: https://trello.com/b/AZaKxCw4/e2-to-do-list-app

## Repository and Docker image

- GitHub repository: https://github.com/Dani-umh/atsd-todolist-daniel-garcia
- Docker Hub image: https://hub.docker.com/r/daniumh/p2-todolistapp

## Technical overview of version 1.1.0

Version **1.1.0** includes all required features and all optional features requested in the exercise:

Required features:

- Menu Bar
- User listing page
- User's description page

Optional features:

- Admin User
- Protection Listing and User Description
- Administrator can block access to users

All features were implemented using the MVC architecture already present in the base project. The implementation included changes in controllers, services, repositories, entities, DTOs and Thymeleaf templates, as well as automatic tests and manual validation of the behaviour of the application.

## Main classes and methods implemented or extended

The most relevant controller changes were made in `LoginController`.  
New endpoints were added to support the user-management functionality:

- `GET /registered` to display the list of registered users
- `GET /registered/{id}` to display the description of a specific user
- `POST /registered/{id}/toggle-enabled` to enable or disable user accounts

The controller logic was also extended to redirect administrator users to the user listing page after login, show a message when a disabled user attempts to log in, and restrict access to user-management pages so that only administrators can access them.

`UsuarioService` was extended with the business logic required by the new functionality.  
The most important additions were:

- `allUsuarios()` to return the list of registered users as DTO objects
- logic to verify whether an administrator already exists
- `toggleEnabledUsuario(Long usuarioId)` to change the enabled status of a user
- extension of `LoginStatus` to include the new value `USER_DISABLED`
- login validation logic to prevent disabled users from accessing the system

The domain model was also updated.  
The entity `Usuario` and the DTO classes `UsuarioData` and `RegistroData` were extended with the fields:

- `admin`
- `enabled`

These fields allow the system to manage administrator privileges and account status.  
The repository layer was extended by adding a method in `UsuarioRepository` to check whether an administrator already exists, ensuring that only one administrator account can be created.

Finally, a new exception class called `AccesoNoPermitidoException` was introduced to return HTTP 401 Unauthorized responses when a non-admin user or a non-authenticated user attempts to access protected pages.

## Thymeleaf templates added and modified

Two new templates were added in version 1.1.0:

- `listaUsuarios.html`
- `descripcionUsuario.html`

Several existing templates were modified to support the new functionality:

- `fragments.html` was extended with a reusable Bootstrap navigation bar fragment
- `about.html` was updated to display different content depending on whether a user is logged in
- `formRegistro.html` was modified to display the “Register as administrator” checkbox only when no administrator exists
- `formLogin.html` was updated to show an error message when a disabled user attempts to log in
- `listaUsuarios.html` was later extended to include action links and Enable/Disable buttons

These changes made it possible to maintain a consistent user interface while integrating the new required and optional functionality.

## Tests implemented

Automatic tests were implemented at both service level and web level.

At service level, `UsuarioServiceTest` was extended to verify:

- listing all users
- retrieving a user by identifier
- login failure for disabled users
- correct toggling of the enabled status of a user

At web level, `UsuarioWebTest` was extended to verify:

- correct rendering of `/registered`
- correct rendering of `/registered/{id}`
- administrator redirection after login
- unauthorized access for non-admin users
- disabled-user login message
- rendering of Enable/Disable buttons
- correct behaviour of `POST /registered/{id}/toggle-enabled`

Manual testing was also performed after each feature implementation.  
The following behaviours were verified manually:

- navigation bar visibility depending on login state
- correct rendering of user listing and description pages
- administrator registration and login behaviour
- access protection for user-management pages
- correct enable and disable behaviour of user accounts
- login behaviour for disabled users

All automatic tests were executed using `mvn test` and completed successfully with `BUILD SUCCESS`.

## Example of relevant source code

A particularly relevant addition was the implementation of access protection for the user-management pages.  
These pages were initially created as new functionality but later had to be restricted so that only administrator users could access them.

The following helper method checks whether the current session user exists and has administrator privileges. If not, an exception is thrown and the request is rejected.

```java
private void comprobarAdmin() {
    Long idUsuario = managerUserSession.usuarioLogeado();

    if (idUsuario == null) {
        throw new AccesoNoPermitidoException("Usuario no logueado");
    }

    UsuarioData usuario = usuarioService.findById(idUsuario);

    if (usuario == null || !usuario.isAdmin()) {
        throw new AccesoNoPermitidoException("Permisos insuficientes");
    }
}
```
This method centralizes the authorization logic and improves maintainability by avoiding repeated permission checks across different controller methods.

## Build and execution

Run the application in development mode:

```bash
./mvnw spring-boot:run
```

Generate the JAR file:

```bash
./mvnw package
```

Run the packaged application:

```bash
java -jar target/todolist-daniel-garcia-1.1.0.jar
```

Build and run the Docker image used for release **1.1.0**:

```bash
docker build -t daniumh/p2-todolistapp:1.1.0 .
docker run --rm -p 8080:8080 daniumh/p2-todolistapp:1.1.0
```

Main application URL:

- http://localhost:8080/login