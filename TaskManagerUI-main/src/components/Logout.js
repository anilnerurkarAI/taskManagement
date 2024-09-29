// src/components/Logout.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Logout() {
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleLogout = async (event) => {
    event.preventDefault();

    try {
      const token = localStorage.getItem('tm-token');
      const name = sessionStorage.getItem('name');
      
      const response = await axios.post(
        'http://localhost:8080/signout',
        { // This is where you provide the request body
          logout: true,
          token: token,
          username: name,
          password: ""
        },
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          withCredentials: true, // Use boolean true, not a string
        }
      );

      if (response.status === 200) {
        // Assuming your backend sends a JWT token on successful login
        const data = response.data;
        localStorage.removeItem('tm-token'); // Remove token from local storage
        sessionStorage.removeItem('name'); // Remove token from session storage
        navigate('/login'); // Redirect to login page
        
        // ... inside handleSubmit after localStorage.setItem('tm-token', data.token);
        window.location.reload(); 

      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Invalid token');
        localStorage.removeItem('tm-token'); // Remove token from local storage
        sessionStorage.removeItem('name'); // Remove token from session storage
        navigate('/login'); // Redirect to login page
      }
    } catch (error) {
      setError('An error occurred. Please try again later.');
      localStorage.removeItem('tm-token'); // Remove token from local storage
      sessionStorage.removeItem('name'); // Remove token from session storage
      navigate('/login'); // Redirect to login page
    }
  };


  return (

    <div className="container"> {/* Use Bootstrap container */}
      <div className="row">
        <div className="col-md-6 col-md-offset-3"> {/* Center the form */}
            <div className="form-group">
            <div className="col-sm-offset-2 col-sm-10">
              <div className="text-center">
                <button onClick={handleLogout} className="btn btn-primary">Logout</button>
            </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
  );
}

export default Logout;
