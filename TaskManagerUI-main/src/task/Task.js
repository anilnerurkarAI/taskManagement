import axios from 'axios';
import React, { useState, useEffect } from 'react';

function Task() {
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [newTask, setNewTask] = useState({ title: '', description: '', status: '', dueDate: '' });
  const [editingTask, setEditingTask] = useState(null);
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const token = localStorage.getItem('tm-token');
        const response = await axios.get(
          'http://localhost:8080/task-manager/tasks',
          {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json',
            },
            withCredentials: true,
          }
        );   
        if (response.status === 200) {
          setTasks(response.data);
        } else {
          setError('Failed to fetch tasks');
        }
      } catch (error) {
        console.error('Error fetching tasks:', error);
        setError('Error fetching tasks');
      } finally {
        setLoading(false);
      }
    };

    fetchTasks();
  }, []);

  const handleAddTask = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('tm-token');
      const response = await axios.post(
        'http://localhost:8080/task-manager/tasks',
        newTask,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          withCredentials: true,
        }
      );
      if (response.status === 201) {
        setTasks([...tasks, response.data]);
        setNewTask({ title: '', description: '', status: '', dueDate: '' });
        setShowForm(false);
      } else {
        setError('Failed to add task');
      }
    } catch (error) {
      console.error('Error adding task:', error);
      setError('Error adding task');
    }
  };

  const handleEditTask = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('tm-token');
      const response = await axios.put(
        `http://localhost:8080/task-manager/tasks/${editingTask.id}`,
        editingTask,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          withCredentials: true,
        }
      );
      if (response.status === 200) {
        setTasks(tasks.map(task => (task.id === editingTask.id ? response.data : task)));
        setEditingTask(null);
        setShowForm(false);
      } else {
        setError('Failed to update task');
      }
    } catch (error) {
      console.error('Error editing task:', error);
      setError('Error editing task');
    }
  };

  const handleDeleteTask = async (taskId) => {
    try {
      const token = localStorage.getItem('tm-token');
      const response = await axios.delete(
        `http://localhost:8080/task-manager/tasks/${taskId}`,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          withCredentials: true,
        }
      );
      if (response.status === 200) {
        setTasks(tasks.filter(task => task.id !== taskId));
      } else {
        setError('Failed to delete task');
      }
    } catch (error) {
      console.error('Error deleting task:', error);
      setError('Error deleting task');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (editingTask) {
      setEditingTask({ ...editingTask, [name]: value });
    } else {
      setNewTask({ ...newTask, [name]: value });
    }
  };

  const handleEditClick = (task) => {
    setEditingTask(task);
    setShowForm(true);
  };

  const handleCancelClick = () => {
    setEditingTask(null);
    setNewTask({ title: '', description: '', status: '', dueDate: '' });
    setShowForm(false);
  };

  if (loading) {
    return <div className="text-center">Loading...</div>;
  }

  return (
    <div className="container">
      <h2 className="text-center">Tasks</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <button className="btn btn-primary mb-3" onClick={() => setShowForm(true)}>Add Task</button>
      {showForm && (
        <form onSubmit={editingTask ? handleEditTask : handleAddTask}>
          <div className="mb-3">
            <label htmlFor="title" className="form-label">Title</label>
            <input
              type="text"
              id="title"
              name="title"
              className="form-control"
              value={editingTask ? editingTask.title : newTask.title}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="description" className="form-label">Description</label>
            <input
              type="text"
              id="description"
              name="description"
              className="form-control"
              value={editingTask ? editingTask.description : newTask.description}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="status" className="form-label">Status</label>
            <input
              type="text"
              id="status"
              name="status"
              className="form-control"
              value={editingTask ? editingTask.status : newTask.status}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="dueDate" className="form-label">Due Date</label>
            <input
              type="date"
              id="dueDate"
              name="dueDate"
              className="form-control"
              value={editingTask ? editingTask.dueDate : newTask.dueDate}
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit" className="btn btn-success">{editingTask ? 'Update' : 'Add'} Task</button>
          <button type="button" className="btn btn-secondary ms-2" onClick={handleCancelClick}>Cancel</button>
        </form>
      )}
      <table className="table table-striped mt-3">
        <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Description</th>
            <th>Status</th>
            <th>Due Date</th> {/* Added Due Date column */}
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.length > 0 ? (
            tasks.map((task) => (
              <tr key={task.id}>
                <td>{task.id}</td>
                <td>{task.title}</td>
                <td>{task.description}</td>
                <td>{task.status}</td>
                <td>{new Date(task.dueDate).toLocaleDateString()}</td> {/* Format the date */}
                <td>
                  <button className="btn btn-primary me-2" onClick={() => handleEditClick(task)}>Edit</button>
                  <button className="btn btn-danger" onClick={() => handleDeleteTask(task.id)}>Delete</button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="text-center">No tasks available</td> {/* Updated colSpan */}
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Task;
