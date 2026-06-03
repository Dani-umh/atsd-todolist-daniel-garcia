# GitFlow Teamwork Documentation

## Team Project

**Student:** Daniel Garcia Moreno

---

# Release 1.3.0

This release introduces a new feature that allows teams to include a description field.

The feature was developed following the GitFlow methodology:

* Issue creation
* Feature branch creation
* Development and testing
* Pull Request review and merge into develop
* Release branch creation
* Production testing
* Merge into master and develop

---

# Changes Introduced

## Team Description Feature

A new attribute called `descripcion` was added to the `Equipo` entity.

The following components were updated:

* Equipo entity
* EquipoData DTO
* EquipoService
* Database schema
* Team listing view
* About page

The application now stores and displays a description associated with each team.

---

# Database Migration

Version 1.2.0 database schema:

* File: `sql/schema-1.2.0.sql`

Version 1.3.0 database schema:

* File: `sql/schema-1.3.0.sql`

Migration script:

* File: `sql/schema-1.2.0-1.3.0.sql`

The migration script adds the new `descripcion` column to the `equipos` table.

---

# Production Deployment

The release was validated using a PostgreSQL Docker container.

Steps performed:

1. Create a PostgreSQL container.
2. Restore a backup generated from version 1.2.0.
3. Execute the migration script.
4. Verify database schema changes.
5. Run the application using the production profile.
6. Verify correct operation of the application.

The deployment was completed successfully.

---

# Production Backup

A production backup was generated after the migration process.

Backup file:

* `sql/sql-backup03062026.sql`

---

# Docker Image

A Docker image was created and pushed to Docker Hub.

Docker image:

https://hub.docker.com/r/daniumh/p2-todolistapp

Tags:

* `1.3.0`
* `latest`

---

# GitFlow Summary

The following GitFlow branches were used:

## Feature Branches

* `43-add-description-field-to-equipo`
* `44-improve-about-page-for-release-1.3.0`

## Release Branch

* `release-1.3.0`

## Main Branches

* `develop`
* `master`

All changes were integrated through Pull Requests and validated using automated tests before merging.

---

# Validation

The following checks were successfully completed:

* Unit tests
* Integration tests
* Database migration verification
* Production deployment verification
* Docker image publication

Release 1.3.0 was successfully completed and deployed.
