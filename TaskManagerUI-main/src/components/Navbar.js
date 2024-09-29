// src/components/Navbar.js
import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  const isLoggedIn = localStorage.getItem('tm-token') !== null; // Check for token
  return (
    <nav>
      <ul>
        <li><Link to="/">Home</Link></li>
        <li><Link to="/task">Tasks</Link></li>
        {!isLoggedIn && ( 
          <>
        <li><Link to="/login">Login</Link></li>
        <li><Link to="/signup">Signup</Link></li>
        </>
        )}

        {isLoggedIn && ( 
          <>
          <li><Link to="/logout">Logout</Link></li>
        </>
        )}
      </ul>
    </nav>
  );
}

export default Navbar;
