import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchWithAuth } from './apiUtils';
import './PlayerForm.css';

function PlayerForm() {
    const [player, setPlayer] = useState({
        firstName: '',
        lastName: '',
        username: ''
    });
    const [isEditMode, setIsEditMode] = useState(false);
    const { playerId } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (playerId) {
            fetchWithAuth(`http://localhost:8080/api/player/${playerId}`)
                .then(response => {
                    if (response.status === 200) {
                        return response.json();
                    } else {
                        return Promise.reject(`Unexpected Status Code: ${response.status}`);
                    }
                })
                .then(data => {
                    setPlayer({
                        firstName: data.firstName,
                        lastName: data.lastName,
                        username: data.username
                    });
                    setIsEditMode(true);
                })
                .catch(error => console.error('Error fetching player:', error));
        }
    }, [playerId]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setPlayer(prevPlayer => ({
            ...prevPlayer,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const method = isEditMode ? 'PUT' : 'POST';
        const url = isEditMode
            ? `http://localhost:8080/api/player/${playerId}`
            : 'http://localhost:8080/api/player';

        fetchWithAuth(url, {
            method,
            body: JSON.stringify({
                ...player,
                playerId: isEditMode ? playerId : undefined
            })
        })
            .then(response => {
                if (response.status === (isEditMode ? 204 : 201)) {
                    navigate('/players');
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .catch(error => console.error('Error saving player:', error));
    };

    return (
        <section className="container">
            <h2 className="mb-4">{isEditMode ? 'Edit Player' : 'Add Player'}</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Username</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={player.username}
                        readOnly={isEditMode}  // Make readOnly only if in edit mode
                        className={`form-control ${isEditMode ? 'readonly-field' : ''}`}  // Apply class if in edit mode
                        onChange={handleInputChange} // Allow changes if not readOnly
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="firstName">First Name</label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={player.firstName}
                        onChange={handleInputChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="lastName">Last Name</label>
                    <input
                        type="text"
                        id="lastName"
                        name="lastName"
                        value={player.lastName}
                        onChange={handleInputChange}
                        className="form-control"
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary">
                    {isEditMode ? 'Update Player' : 'Add Player'}
                </button>
            </form>
        </section>
    );
}

export default PlayerForm;
