# Gemini Task Management

This project is a full-stack task management application built with a Spring Boot backend and a React frontend. It allows users to manage their tasks efficiently with features like user authentication, task creation, editing, deletion, and status management.

## Features

- **User Authentication:** Securely register and log in to manage your tasks.
- **Task Management:**
    - Create new tasks with titles, descriptions, and due dates.
    - Edit existing task details.
    - Mark tasks as complete or delete them.
    - Filter and sort tasks based on different criteria.
- **Status Management:** Categorize tasks with statuses like "To-Do," "In Progress," and "Completed."

## Technologies Used

**Backend:**

- Java
- Spring Boot
- Spring Data JPA
- Spring Security (for authentication and authorization)
- Database (e.g., MySQL, PostgreSQL, H2)

**Frontend:**

- React
- React Router DOM
- Axios (for API communication)
- Material-UI or Bootstrap (for styling)

## Project Structure

The project is divided into two main parts:

- **backend:** Contains the Spring Boot backend application.
- **frontend:** Contains the React frontend application.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Node.js and npm (or yarn)
- Database of your choice (e.g., MySQL, PostgreSQL, H2)

### Running the Application

1. **Backend:**
   - Navigate to the `TaskManager` directory.
   - Configure your database connection in the `application.properties` 
   - Build the project using Gradle: `./gradlew build`
   - Run the Spring Boot application: `./gradlew bootRun`
2. **Frontend:**
   - Navigate to the `taskmanagerUI-main` directory.
   - Install dependencies: `npm install`
   - Start the development server: `npm start`

The frontend should now be accessible at `http://localhost:3000` (or the port specified in your React configuration).

## API Documentation

Once the backend is running, you can access the API documentation (if available) at:

- `http://localhost:8080/swagger-ui.html` (if you're using SpringFox Swagger)

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Make your changes and commit them: `git commit -m "Add your feature"`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Submit a pull request.

## License

This project is licensed under the [License Name] License - see the [LICENSE.md](LICENSE.md) file for details.
