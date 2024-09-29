// src/pages/Signup.js
import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Signup() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [email, setEmail] = useState('');
  const [mobile, setMobile] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/signup', 
        { 
          'username': username,
           'password': password,
           'email': email,
           'mobile': mobile
        },  
        {
          headers: {
            'Content-Type': 'application/json',
          }
        }
        
      );

      if (response.status === 200 || response.status === 201) {
        // Assuming your backend sends a JWT token on successful login
        navigate('/login'); // Redirect to home or dashboard
       
      } else {
        // This branch may not be necessary since status 200 is handled above.
        // If the server responds with an error status code
        setError(response.data.message || 'Server error');
      }
    } catch (error) {
      setError('An error occurred. Please try again later.');
    }
  };

  return (
    <div className="container"> {/* Use Bootstrap container */}
      <div className="row">
        <div className="col-md-6 col-md-offset-3"> {/* Center the form */}
          <h2 className="text-center">Signup</h2>
          {error && <div className="alert alert-danger">{error}</div>}
          <form onSubmit={handleSubmit} className="form-horizontal"> 
            {/* Use Bootstrap form classes */}
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
              <label htmlFor="confirmPassword" className="col-sm-2 control-label">Confirm Password:</label>
              <div className="col-sm-10">
                <input required
                  type="password"
                  id="confirmPassword"
                  className="form-control"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                />
              </div>
            </div>

            
            <div className="form-group">
              <label htmlFor="email" className="col-sm-2 control-label">Email :</label>
              <div className="col-sm-10">
                <input required
                  type="email"
                  id="email"
                  className="form-control"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>
            </div>


            <div className="form-group">
              <label htmlFor="mobile" className="col-sm-2 control-label">Mobile :</label>
              <div className="col-sm-10">
                <input required
                  type="number"
                  id="mobile  "
                  className="form-control"
                  value={mobile}
                  onChange={(e) => setMobile(e.target.value)}
                />
              </div>
            </div>



            {/* Repeat similar structure for other form fields */}

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

export default Signup;
