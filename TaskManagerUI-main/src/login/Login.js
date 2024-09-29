import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/login', 
        { 
          'username': username,
           'password': password 
          },
        {
          headers: {
            'Content-Type': 'application/json',
          }
        }
        
      );

      if (response.status === 200) {
        // Assuming your backend sends a JWT token on successful login
        const data = response.data;
        localStorage.setItem('tm-token', data.token); // Save the token in localStorage
        sessionStorage.setItem('name', data.name); // Save the token in sessionStorage
        navigate('/'); // Redirect to home or dashboard
        // Optionally reload the page
        window.location.reload(); 
      } else {
        // This branch may not be necessary since status 200 is handled above.
        // If the server responds with an error status code
        setError(response.data.message || 'Invalid credentials');
      }
    } catch (error) {
      setError('Invalid credentials. Please try again later.');
    }
  };

  return (
    <div className="container"> {/* Use Bootstrap container */}
    <div className="row">
      <div className="col-md-6 col-md-offset-3"> {/* Center the form */}
        <h2 className="text-center">Login</h2>
        {error && <div className="alert alert-danger">{error}</div>}
        <form onSubmit={handleSubmit} className="form-horizontal"> 


        <div className="form-group">
              <label htmlFor="username" className="col-sm-2 control-label">Username:</label>
              <div className="col-sm-10">
                <input required
                  type="text"
                  id="username"
                  className="form-control"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="password" className="col-sm-2 control-label">Password:</label>
              <div className="col-sm-10">
                <input required
                  type="password"
                  id="password"
                  className="form-control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
            </div>

        <div className="form-group">
              <div className="col-sm-offset-2 col-sm-10">
                <button type="submit" className="btn btn-primary">Signup</button>
              </div>
            </div>




        </form>
        </div>
      </div>
    </div>
        
  );
}

export default Login;
