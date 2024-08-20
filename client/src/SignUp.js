import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function SignUp() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();

        const signupUrl = 'http://localhost:8080/api/user/register';

        fetch(signupUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        })
            .then(response => {
                if (response.status === 201) {
                    return response.json();
                } else if (response.status === 400) {
                    return response.json().then(err => Promise.reject(err));
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .then(data => {
                console.log('User ID:', data.appUserId); // Log user ID for debugging
                console.log("Received JWT token:", data.jwt_token);
                localStorage.setItem('authToken', data.jwt_token);
                localStorage.setItem('username', username); // Save username as well
                navigate('/playerDashboard');
            })
            .catch(err => {
                console.log(err);
                setError(Array.isArray(err) ? err.join(', ') : 'Sign Up failed. Please try again.');
            });
    };

    return (
        <>
            <div className="form-container">
                <h1 className="page-title">Sign Up</h1>

                <div className="signup-container">
                    <form className="signup-form" onSubmit={handleSubmit}>
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
                            <button type="submit" className="btn btn-primary">Sign Up</button>
                        </div>
                    </form>
                </div>

                {error && <p className="error-message">{error}</p>}
            </div>
        </>
    );
}

export default SignUp;
