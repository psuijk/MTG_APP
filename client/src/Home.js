import React, { useState, useEffect } from 'react';
import { fetchWithAuth } from "./apiUtils";
import { Link, useNavigate } from 'react-router-dom';
import './Home.css';

function Home() {
    const [games, setGames] = useState([]);
    const [decks, setDecks] = useState({});
    const [error, setError] = useState(null);
    const [playerCount, setPlayerCount] = useState(4); // State to track the current player count
    const navigate = useNavigate(); // Hook for navigation

    useEffect(() => {
        fetchGames(playerCount);
    }, [playerCount]);

    const fetchGames = (count) => {
        const gamesUrl = `http://localhost:8080/api/game`;
        fetchWithAuth(gamesUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                // Filter games by player count
                const filteredGames = data.filter(game => game.playerCount === count);
                setGames(filteredGames);
                filteredGames.forEach(game => {
                    game.decks.forEach(deckId => fetchDeckName(deckId));
                    fetchDeckName(game.winnerDeckId);
                });
            })
            .catch(err => {
                console.error('Error fetching games:', err);
                setError('Failed to fetch games. Please try again later.');
            });
    };

    const fetchDeckName = (deckId) => {
        fetchWithAuth(`http://localhost:8080/api/deck/${deckId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(deck => {
                setDecks(prevDecks => ({
                    ...prevDecks,
                    [deckId]: deck.name
                }));
            })
            .catch(err => {
                console.error(`Error fetching deck ${deckId}:`, err);
                setError(`Failed to fetch deck information for deck ID ${deckId}.`);
            });
    };

    const handleDelete = (gameId) => {
        if (window.confirm("Are you sure you want to delete this game?")) {
            fetchWithAuth(`http://localhost:8080/api/game/${gameId}`, {
                method: 'DELETE',
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    // Remove the game from the state
                    setGames(games.filter(game => game.gameId !== gameId));
                })
                .catch(err => {
                    console.error('Error deleting game:', err);
                    setError('Failed to delete game. Please try again later.');
                });
        }
    };

    const handleEdit = (gameId) => {
        navigate(`/editGame/${gameId}`);
    };

    return (
        <section className="container">
            <h2 className="mb-4">Welcome to the MTG Gametracker</h2>
            <h1 className="mb-2">Recent Games</h1>
            <Link to="/gameForm" className="btn btn-primary mb-3">Add a New Game</Link>
            {error && <p className="error">{error}</p>}
            <div className="tabs">
                <button
                    className={`tab-button ${playerCount === 4 ? 'active' : ''}`}
                    onClick={() => setPlayerCount(4)}
                >
                    4 Player Games
                </button>
                <button
                    className={`tab-button ${playerCount === 5 ? 'active' : ''}`}
                    onClick={() => setPlayerCount(5)}
                >
                    5 Player Games
                </button>
            </div>
            <table className="table table-striped table-hover">
                <thead className="thead-dark">
                    <tr>
                        <th>Date</th>
                        <th>Winner</th>
                        {[...Array(playerCount)].map((_, index) => (
                            <th key={index}>Deck {index + 1}</th>
                        ))}
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {games.map(game => (
                        <tr key={game.gameId}>
                            <td>{game.datePlayed}</td>
                            <td>{decks[game.winnerDeckId] || 'Loading...'}</td>
                            {[...Array(playerCount)].map((_, index) => (
                                <td key={index}>
                                    {game.decks[index] ? decks[game.decks[index]] || 'Loading...' : '-'}
                                </td>
                            ))}
                            <td>
                                <div className="action-buttons">
                                    <button
                                        className="btn btn-edit me-2"
                                        onClick={() => handleEdit(game.gameId)}
                                    >
                                        Edit
                                    </button>
                                    <button
                                        className="btn btn-delete"
                                        onClick={() => handleDelete(game.gameId)}
                                    >
                                        Delete
                                    </button>
                                </div>
                            </td>

                        </tr>
                    ))}
                </tbody>
            </table>
        </section>
    );
}

export default Home;
