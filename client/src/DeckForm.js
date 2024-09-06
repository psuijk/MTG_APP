import React, { useState, useEffect } from 'react';
import { fetchWithAuth } from './apiUtils';
import { useNavigate } from 'react-router-dom';  // Import useNavigate for navigation
import './DeckForm.css';

const DeckForm = () => {
    const [players, setPlayers] = useState([]);
    const [deckName, setDeckName] = useState('');
    const [commander, setCommander] = useState('');
    const [selectedPlayer, setSelectedPlayer] = useState('');
    const [active, setActive] = useState(true);
    const navigate = useNavigate();  // Initialize navigate hook

    useEffect(() => {
        // Fetch the list of players when the component loads
        fetchWithAuth('http://localhost:8080/api/player')
            .then((response) => response.json())
            .then((data) => {
                setPlayers(data);
            })
            .catch((error) => {
                console.error('Error fetching players:', error);
            });
    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();

        const newDeck = {
            playerId: selectedPlayer,
            name: deckName,
            active,
            commanderId: commander
        };

        try {
            const response = await fetchWithAuth('http://localhost:8080/api/deck', {
                method: 'POST',
                body: JSON.stringify(newDeck),
            });

            if (response.ok) {
                // Navigate to /home on success
                navigate('/playerDashboard');
            } else {
                // Log error message on failure
                console.error("Error creating deck:", await response.text());
            }
        } catch (error) {
            // Handle any unexpected errors
            console.error("Error during fetch:", error);
        }
    };

    return (
        <div className="add-deck-container">
            <form className="add-deck-form" onSubmit={handleSubmit}>
                <h2>Add New Deck</h2>
                <div className="form-group">
                    <label>Deck Name:</label>
                    <input
                        type="text"
                        value={deckName}
                        onChange={(e) => setDeckName(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Commander Name:</label>
                    <input
                        type="text"
                        value={commander}
                        onChange={(e) => setCommander(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Player:</label>
                    <select
                        value={selectedPlayer}
                        onChange={(e) => setSelectedPlayer(e.target.value)}
                        required
                    >
                        <option value="">Select a player</option>
                        {players.map((player) => (
                            <option key={player.playerId} value={player.playerId}>
                                {player.firstName} {player.lastName}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Active:</label>
                    <input
                        type="checkbox"
                        checked={active}
                        onChange={(e) => setActive(e.target.checked)}
                    />
                </div>

                <button type="submit">Add Deck</button>
            </form>
        </div>
    );
};

export default DeckForm;
