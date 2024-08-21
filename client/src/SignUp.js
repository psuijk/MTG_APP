import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './SignUp.css';
import { Link } from 'react-router-dom';

function SignUp() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isNewPlayer, setIsNewPlayer] = useState(false);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();

        const url = isNewPlayer
            ? 'http://localhost:8080/api/user/register/newplayer'
            : 'http://localhost:8080/api/user/register';

        const body = {
            username,
            password,
            ...(isNewPlayer && { firstName, lastName }), // Include firstName and lastName if it's a new player
        };

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
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
                navigate('/login');
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

                        <div className="mb-3">
                            <label htmlFor="newPlayer">
                                <input
                                    type="checkbox"
                                    id="newPlayer"
                                    checked={isNewPlayer}
                                    onChange={(e) => setIsNewPlayer(e.target.checked)}
                                />
                                New Player
                            </label>
                        </div>

                        {isNewPlayer && (
                            <>
                                <div className="mb-3">
                                    <label htmlFor="firstName">First Name</label>
                                    <input
                                        type="text"
                                        id="firstName"
                                        name="firstName"
                                        value={firstName}
                                        onChange={(e) => setFirstName(e.target.value)}
                                    />
                                </div>

                                <div className="mb-3">
                                    <label htmlFor="lastName">Last Name</label>
                                    <input
                                        type="text"
                                        id="lastName"
                                        name="lastName"
                                        value={lastName}
                                        onChange={(e) => setLastName(e.target.value)}
                                    />
                                </div>
                            </>
                        )}

                        <div className="button-container">
                            <button type="submit" className="btn btn-primary">Sign Up</button>
                        </div>
                    </form>
                </div>

                {error && <p className="error-message">{error}</p>}
                <Link to="/">Go Back</Link>
            </div>
        </>
    );
}

export default SignUp;
