# README_TDD

## Introduction

This document describes the implementation process followed for user stories 008 and 009 of the ToDoList application developed for the subject Agile Techniques for Software Development.

The development was performed following the TDD and continuous integration methodology required in the assignment. Each feature was implemented incrementally using GitHub issues, Trello cards, feature branches, automatic tests and pull requests before merging into the master branch.

User story 008 focused on team visualization and navigation features, including team listing pages and team member pages.

User story 009 focused on team membership management, allowing users to join and leave teams through the web interface and implementing the required service-layer logic using TDD.

User story 010 (Team management) was not implemented because it was an optional feature.

## User Story 008 — View and controller list of teams

### Objective

The goal of this user story was to implement the controller and view layer required to display the list of teams and allow users to navigate to the member list of each team.

### Main functionality implemented

- Team listing page (`/equipos`)
- Team member page (`/equipos/{id}`)
- Navigation link in the navbar
- Thymeleaf templates for team visualization
- Controller endpoints for listing teams and displaying team members
- Web tests for controller validation
### Classes and endpoints implemented

The controller responsible for the new functionality was `EquipoController`.

The following endpoints were implemented:

- `GET /equipos`
    - Displays the list of teams available in the system.

- `GET /equipos/{id}`
    - Displays the members that belong to a specific team.

The endpoint `/equipos` retrieves all teams ordered alphabetically using the service layer and renders them using the `listaEquipos.html` Thymeleaf template.

The endpoint `/equipos/{id}` retrieves the selected team and its associated users and renders the information using the `usuariosEquipo.html` template.

The controller also retrieves the logged-in user through `ManagerUserSession` in order to maintain the navigation bar and session information consistent with the rest of the application.

### Thymeleaf templates

Two main Thymeleaf templates were used for this functionality:

- `listaEquipos.html`
- `usuariosEquipo.html`

The template `listaEquipos.html` displays all teams in a Bootstrap table and provides clickable links for navigating to the member page of each team.

The template `usuariosEquipo.html` displays the users that belong to the selected team and includes navigation back to the team list.

The navigation bar fragment (`fragments.html`) was also modified to include the new “Teams” option in the menu.

### Service layer used

The controller layer reused methods already implemented previously in `EquipoService`.

The following service methods were used:

- `findAllOrdenadoPorNombre()`
- `recuperarEquipo(Long id)`
- `usuariosEquipo(Long id)`

These methods provide the required business logic and data retrieval functionality while keeping the controller layer simple and focused on HTTP request handling.

### Web tests implemented

Automatic web tests were implemented in `EquipoWebTest`.

The implemented tests verify:

- Correct rendering of the team listing page.
- Correct rendering of the team member page.
- Correct visualization of team names.
- Correct visualization of users belonging to teams.

The tests were implemented using:

- `MockMvc`
- `SpringBootTest`
- `AutoConfigureMockMvc`
- mocked `ManagerUserSession`

The tests simulate authenticated users and validate the generated HTML responses returned by the controller endpoints.

### Manual validation

Manual testing was also performed after implementing the functionality.

The following behaviours were validated manually:

- The “Teams” option appears correctly in the navigation bar.
- The `/equipos` endpoint displays the list of teams.
- Team names are clickable links.
- The `/equipos/{id}` endpoint displays the users belonging to the selected team.
- Navigation between pages works correctly.
- Session information remains consistent while navigating between pages.

All automatic tests were executed successfully using:


```bash
./mvnw test
```
The project finished with `BUILD SUCCESS`.

### Example of relevant source code

One particularly relevant part of the implementation was the endpoint used to display the members of a team.

```java
@GetMapping("/equipos/{id}")
public String usuariosEquipo(@PathVariable(value = "id") Long idEquipo, Model model) {

    Long idUsuario = managerUserSession.usuarioLogeado();

    UsuarioData usuario = usuarioService.findById(idUsuario);
    EquipoData equipo = equipoService.recuperarEquipo(idEquipo);
    List<UsuarioData> usuarios = equipoService.usuariosEquipo(idEquipo);

    model.addAttribute("usuario", usuario);
    model.addAttribute("equipo", equipo);
    model.addAttribute("usuarios", usuarios);

    return "usuariosEquipo";
}

```

This controller method retrieves the logged-in user, the selected team and all associated users before rendering the `usuariosEquipo.html` template.

---

## User Story 009 — Manage team membership

### Objective

The objective of this user story was to allow users to join and leave teams through the web interface while implementing the required service-layer logic using TDD.

This functionality required modifications in the service layer, controller layer, Thymeleaf templates and automatic tests.

### Main functionality implemented

- Join a team
- Leave a team
- Service-layer method to remove users from teams
- Validation for users that do not belong to teams
- Join and Leave buttons in the web interface
- Automatic tests for service and controller layers

### TDD process in the service layer

The service-layer implementation followed the TDD methodology required in the assignment.

The first step was creating new failing tests in `EquipoServiceTest` for the new functionality.

The following scenarios were tested:

- Removing a user from a team correctly
- Throwing an exception when trying to remove a user that does not belong to the team
- Maintaining the many-to-many relationship consistency between users and teams

After implementing the failing tests, the service layer was modified incrementally until all tests passed successfully.

### Service methods implemented

The main method implemented in `EquipoService` was:

```java
@Transactional
public void eliminarUsuarioDeEquipo(Long id, Long id1)
```

This method:

- Retrieves the team and user from the repositories
- Validates that both entities exist
- Checks whether the user belongs to the team
- Removes the relationship from both sides of the many-to-many association

The implementation follows the requirement specified in the assignment indicating that both sides of the relationship must be updated manually when removing users from teams.

### Entity modifications

The entity `Equipo` was extended with a new helper method:

```java
public void removeUsuario(Usuario usuario) {
    this.getUsuarios().remove(usuario);
    usuario.getEquipos().remove(this);
}
```

This method guarantees that the many-to-many relationship remains synchronized correctly in memory and in the database.

### Controller and view implementation

The controller and view layer were also extended to support team membership management.

The functionality implemented includes:

- Join buttons
- Leave buttons
- Controller endpoints for joining teams
- Controller endpoints for leaving teams
- Conditional visualization depending on membership state

The web interface allows authenticated users to participate in teams dynamically through the browser.

### Web tests implemented

Automatic tests were implemented for both the service layer and the controller layer.

The service tests validate:

- Correct removal of users from teams
- Exception handling when removing non-member users
- Relationship consistency between users and teams

The controller tests validate:

- Correct rendering of team pages
- Join team functionality
- Leave team functionality
- Correct visualization of buttons depending on membership state

The tests were implemented using:

- `SpringBootTest`
- `MockMvc`
- `MockBean`
- `JUnit`
- `AssertJ`

All tests were executed successfully using:

```bash
./mvnw test
```

The project finished with `BUILD SUCCESS`.

### Manual validation

Manual testing was also performed after implementing the functionality.

The following behaviours were verified manually:

- Users can join teams successfully
- Users can leave teams successfully
- Join and Leave buttons appear correctly
- Team membership updates dynamically
- Navigation between pages works correctly
- Team member lists update correctly after changes

### Example of relevant source code

The following method was one of the most important additions implemented during user story 009.

```java
public void removeUsuario(Usuario usuario) {
    this.getUsuarios().remove(usuario);
    usuario.getEquipos().remove(this);
}
```

This helper method guarantees that both sides of the many-to-many relationship remain synchronized when removing a user from a team.

Without this synchronization, Hibernate would not update the relationship correctly in the database.

## Final version and delivery

The final delivered version of the project is `1.2.0`.

The application includes:

- All mandatory features from user stories 008 and 009
- Continuous integration workflows using GitHub Actions
- Docker support
- Automatic tests
- Thymeleaf views
- MVC architecture
- TDD-based service-layer implementation

The optional user story 010 was intentionally not implemented.

The final project was prepared for delivery by:

- Running `mvn clean`
- Generating the final JAR file
- Executing all tests successfully
- Building the Docker image
- Publishing the final code to GitHub
- Preparing the final ZIP containing the `.git` folder and workflows

## Repository and Docker image

- GitHub repository: https://github.com/Dani-umh/atsd-todolist-daniel-garcia
- Docker Hub image: https://hub.docker.com/r/daniumh/p2-todolistapp