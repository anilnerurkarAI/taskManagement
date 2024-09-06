import React, { useState, useEffect } from "react";
import "./App.css";

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

import "bootstrap/dist/css/bootstrap.min.css";

function App() {
  const [activeView, setActiveView] = useState("form");
  const [taskNameError, setTaskNameError] = useState("");
  const [tasks, setTasks] = useState([]);
  const [selectedDueDate, setSelectedDueDate] = useState(new Date());
  const [selectedEndDate, setSelectedEndDate] = useState(new Date()); // State for selected end date
  const [editingTask, setEditingTask] = useState(null);

  const [isLoggedIn, setIsLoggedIn] = useState(true);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [authError, setAuthError] = useState(null);

  useEffect(() => {
    // Check for existing authentication (e.g., JWT token in local storage)
    const token = localStorage.getItem("token");
    if (token) {
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogin = () => {
    // Send authentication request to your backend
    
    fetch("http://localhost:8080/api/login", { // Replace with your login endpoint
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Invalid username or password");
        }
        return response.json();
      })
      .then((data) => {
        // Store the token in local storage
        localStorage.setItem("token", data.token);
        setIsLoggedIn(true);
        setAuthError(null); // Clear any previous errors
      })
      .catch((error) => {
        console.error("Login error:", error);
        setAuthError("Invalid username or password");
      });
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    setIsLoggedIn(false);
  }; 


  useEffect(() => {
    console.log("Fetching tasks from:", "http://localhost:8080/"); // Log before fetching
    fetch("http://localhost:8080/")
      .then((response) => {
        console.log("Response:", response); // Log the entire response object
        return response.json();
      })
      .then((data) => {
        console.log("Data received from API:", data); // Log data before updating state
        setTasks(data);
        console.log("Tasks state updated:", tasks); // Log tasks state after update (might not reflect immediately due to async nature)
      })
      .catch((error) => console.error("Error fetching tasks:", error));
  }, [tasks]);

  const handleNavClick = (view) => {
    setActiveView(view);
  };

  const handleAddTask = () => {
    // Create a new task object
    const newTask = {
      name: document.getElementById("taskName").value,
      description: document.getElementById("description").value,
      selectedDueDate: selectedDueDate.toDateString(), // Format the date
      selectedEndDate: selectedEndDate.toDateString(), // Format the date
      completed: false 
    };

    // Send a POST request to your Spring Boot backend
    fetch('http://localhost:8080/api/tasks', { // Assuming '/api/tasks' is your endpoint
      method: 'POST',
      headers: {
        'Content-Type': 'application/json' 
      },
      body: JSON.stringify(newTask)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Could not add task'); // Handle errors appropriately
      }

      document.getElementById("name").value = "";
      document.getElementById("description").value = "";
      setSelectedDueDate(new Date());

      return response.json(); // Parse the response as JSON if needed

    })
    .catch(error => {
      console.error('Error adding task:', error);
      // Handle errors, e.g., display an error message to the user
    });
  };
  
  const handleDeleteTask = (index) => {
    // Get the ID of the task to delete
    const taskIdToDelete = tasks[index].id; // Assuming your tasks have an '_id' field from MongoDB
  
    // Send a DELETE request to your backend
    fetch(`http://localhost:8080/api/tasks/${taskIdToDelete}`, { 
      method: 'DELETE' 
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Could not delete task'); // Handle errors appropriately
      }
      // Update the UI after successful deletion
      const updatedTasks = [...tasks];
      updatedTasks.splice(index, 1);
      setTasks(updatedTasks);
    })
    .catch(error => {
      console.error('Error deleting task:', error);
      // Handle errors, e.g., display an error message to the user
    });
  };
  
  const handleEditTask = (task) => {
    setEditingTask(task);
    document.getElementById("name").value = task.name;  
    document.getElementById("description").value = task.description;
    setSelectedDueDate(new Date(task.selectedDueDate));    
    setSelectedEndDate(new Date(task.selectedEndDate));
    setActiveView("form"); // Switch to the form view
  }; 
  
  return (
    <><div
      className="App"
      style={{
        backgroundImage: "url(task-manager-background.jpg)",
        backgroundSize: "cover",
        backgroundRepeat: "no-repeat",
      }}
    >
      <nav className="navbar navbar-expand-lg navbar-light bg-light">
        <a className="navbar-brand" href="#">
          Task Manager
        </a>
        <ul className="navbar-nav">
          <li className="nav-item">
            <button
              className={`nav-link ${activeView === "form" ? "active" : ""}`}
              onClick={() => handleNavClick("form")}
            >
              Create Task
            </button>
          </li>
          <li className="nav-item">
            <button
              className={`nav-link ${activeView === "list" ? "active" : ""}`}
              onClick={() => handleNavClick("list")}
            >
              View-Edit Tasks
            </button>
          </li>
        </ul>
      </nav>
  {isLoggedIn && ( // Show task form and other UI only if logged in
      <div className="container mt-4">
        {activeView === "form" && (
          <div className="row">
            <div className="col-md-6 offset-md-3">
              <div className="form-group">
                <label htmlFor="taskName">Task Name:</label>
                <input
                  type="text"
                  className="form-control"
                  id="taskName" />

              </div>
              <div className="form-group">
                <label htmlFor="description">Description:</label>
                <input type="text" className="form-control" id="description" />
              </div>

              <div className="form-group">
                <label htmlFor="dueDate">Due Date:</label>
                <input
                  type="date"
                  className="form-control"
                  id="selectedDueDate"
                  value={selectedDueDate.toISOString().slice(0, 10)} // Format date for input field
                  onChange={(e) => setSelectedDueDate(new Date(e.target.value))} />
              </div>

              <button
                type="button"
                className="btn btn-primary"
                onClick={handleAddTask}
              >
                Add Task
              </button>

              {activeView === "list" && (
                <div>
                  <h2>Task List</h2>
                  <div className="row">
                    {tasks.map((task, index) => {
                      console.log(`Rendering task at index ${index}:`, task); // Log each task object
                      return (
                        <div key={index} className="col-md-4 mb-3">
                          {/* ... card content */}
                        </div>
                      );
                    })}
                  </div>
                </div>
              )}
            </div>
          </div>
        )}

        {activeView === "list" && (
          <div>
            <h2>Task List</h2>
            <table className="table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Start Date</th>
                  <th>End Date</th>
                  <th>complete?</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {tasks.map((task, index) => (
                  <tr key={index}>
                    <td>{task.name}</td>
                    <td>{task.description}</td>
                    <td>{task.dueDate}</td>
                    <td>{task.completionDate}</td>
                    <td>{task.completed ? "Yes" : "No"}</td>
                    <td>
                      <button
                        className="btn btn-warning btn-sm mr-2"
                        onClick={() => handleEditTask(task)}
                      >
                        Edit
                      </button>
                      <button
                        className="btn btn-danger btn-sm"
                        onClick={() => handleDeleteTask(index)}
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
            )}
    </div><div className="App">
        {/* ... (your existing JSX) */}

        {!isLoggedIn ? (
          <div>
            <h2>Login</h2>
            {authError && <div className="alert alert-danger">{authError}</div>}
            <div className="form-group">
              <label htmlFor="username">Username:</label>
              <input
                type="text"
                className="form-control"
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)} />
            </div>
            <div className="form-group">
              <label htmlFor="password">Password:</label>
              <input
                type="password"
                className="form-control"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)} />
            </div>
            <button className="btn btn-primary" onClick={handleLogin}>
              Login
            </button>
          </div>
        ) : (
          <div>
            {/* ... (your existing task management UI) */}
            <button className="btn btn-danger" onClick={handleLogout}>
              Logout
            </button>
          </div>
        )}
      </div></>
  );
}

export default App;
