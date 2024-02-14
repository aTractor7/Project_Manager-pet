# Project Management Software
Project management software provides a set of features for effective task, requirement, project tracking, team listing, and planning methodologies like Agile/Kanban/RUP.

## Environment Requirements
Java JDK 17+
Maven 3.6+
MySQL 8.0+

## Functionality
Task, requirement, and project tracking
Team listing
Current tasks
Planning methodologies: Agile/Kanban/RUP
Ability to attach files to tasks
Storage of executables for each version

## API Endpoints

### Authentication

- `POST /auth/registration`: Create a new user.
**Request Body**
```json
{
  "username": "example_user",
  "password": "password123"
}
```

### Projects

- `GET /project`: Retrieve all projects for user.
- `GET /project/{id}`: Retrieve a specific project by ID.
- `POST /project`: Create a new project.
- `PUT /project/{id}`: Update an existing project.
- `DELETE /project/{id}`: Delete a project by ID.

**Request Body for POST & PUT**
```json
{
  "title": "example_title",
  "description": "example_description",
  "status": "PENDING",
  "methodology": "RUP"
}
```

### Tasks

- `GET /task/{projetcId}`: Retrieve all tasks.
- `GET /task/{id}`: Retrieve a specific task by ID.
- `POST /task`: Create a new task.
- `PUT /task/{id}`: Update an existing task.
- `DELETE /task/{id}`: Delete a task by ID.

**Request Body for POST & PUT**
```json
{
  "title": "example_title",
  "description": "example_description",
  "status": "PENDING",
  "deadline": "2222-01-01 00:00:00",
  "priority": 5,
  "projectId": 1
}
```

### File

- `GET /file/`: Retrieve all files .
- `GET /file/{id}`: Retrieve a specific file by ID for a project.
- `POST /file/{taskId}`: Upload a new file for a task.
- `DELETE /file/{id}`: Delete a file by ID.
