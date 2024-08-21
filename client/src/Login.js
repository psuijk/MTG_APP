import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Login.css'; // Import the new CSS file

function Login() {
    const [username, setUsername] = useState(''); // Update variable name to 'username'
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();

        const loginUrl = 'http://localhost:8080/api/user/authenticate';

        fetch(loginUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }), // Send 'username' instead of 'email'
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 403) {
                    return Promise.reject('Invalid username or password.');
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .then(data => {
                // Assuming the token is in data.jwt_token
                console.log("Received JWT token:", data.jwt_token);
                console.log("Storing this username:", username);
                localStorage.setItem('authToken', data.jwt_token);
                localStorage.setItem('username', username); // Save username as well
                navigate('/playerDashboard'); // Redirect to home page or desired page
            })
            .catch(error => {
                console.log(error);
                setError('Login failed. Please try again.');
            });
    };

    return (
        <>
            <div className="form-container">
                <h1 className="page-title">MTG Gametracker</h1>

                <div className="login-container">
                    <form className="login-form" onSubmit={handleSubmit}>
                        <label htmlFor="username">Username</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            required
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />

                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />

                        <div className="button-container">
                            <button type="submit" className="btn btn-primary" id="loginButton">Login</button>
                        </div>
                    </form>
                </div>

                {error && <p className="error-message">{error}</p>}

                <Link to="/">Go Back</Link>
            </div>
        </>
    );
}

export default Login;
