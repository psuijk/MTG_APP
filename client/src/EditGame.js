import React, { useState, useEffect } from 'react';
import { fetchWithAuth } from './apiUtils'; // Adjust path if needed
import './GameForm.css';
import { useNavigate, useParams } from 'react-router-dom';

function EditGameForm() {
    const { gameId } = useParams();
    const [players, setPlayers] = useState([]);
    const [game, setGame] = useState(null);
    const [selectedPlayers, setSelectedPlayers] = useState([]);
    const [selectedDecks, setSelectedDecks] = useState([]);
    const [winningDeck, setWinningDeck] = useState('');
    const [datePlayed, setDatePlayed] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        console.log("hello");
        const fetchPlayers = async () => {
            try {
                const response = await fetchWithAuth('http://localhost:8080/api/player');
                const playersData = await response.json();
                setPlayers(playersData);
            } catch (error) {
                console.error("Error fetching players:", error);
            }
        };

        fetchPlayers();
    }, []); // Fetch players only once

    useEffect(() => {
        const fetchGame = async () => {
            try {
                const response = await fetchWithAuth(`http://localhost:8080/api/game/${gameId}`);
                const gameData = await response.json();
                setGame(gameData);
                setDatePlayed(gameData.datePlayed);
                setWinningDeck(gameData.winnerDeckId);

                if (players.length > 0) {
                    const selectedPlayersData = gameData.decks.map(deckId => {
                        const player = players.find(p => p.playerDecks.some(deck => deck.deckId === deckId));
                        return { playerId: player?.playerId || null, deckId };
                    });
                    setSelectedPlayers(selectedPlayersData);
                }
            } catch (error) {
                console.error("Error fetching game:", error);
            }
        };

        if (players.length > 0) {
            fetchGame();
        }
    }, [gameId, players.length]); // Run this only when gameId or players are loaded


    const handlePlayerChange = (index, playerId) => {
        const player = players.find(p => p.playerId === parseInt(playerId));
        const newSelectedPlayers = [...selectedPlayers];
        newSelectedPlayers[index] = { playerId: parseInt(playerId), deckId: '' };
        setSelectedPlayers(newSelectedPlayers);
        updateSelectedDecks(newSelectedPlayers);
    };

    const handleDeckChange = (index, deckId) => {
        const selectedPlayer = players.find(player => player.playerId === selectedPlayers[index].playerId);
        const selectedDeck = selectedPlayer?.playerDecks.find(deck => deck.deckId === parseInt(deckId));

        const newSelectedPlayers = [...selectedPlayers];
        newSelectedPlayers[index].deckId = deckId;
        setSelectedPlayers(newSelectedPlayers);
        updateSelectedDecks(newSelectedPlayers, selectedDeck);
    };

    const updateSelectedDecks = (selectedPlayers, newDeck) => {
        const decks = selectedPlayers
            .map(sp => {
                const player = players.find(p => p.playerId === sp.playerId);
                return player?.playerDecks.find(deck => deck.deckId === parseInt(sp.deckId));
            })
            .filter(deck => deck !== undefined);

        if (newDeck) {
            const deckAlreadyExists = decks.find(deck => deck.deckId === newDeck.deckId);
            if (!deckAlreadyExists) {
                decks.push(newDeck);
            }
        }

        setSelectedDecks(decks);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const deckIds = selectedPlayers.map(sp => sp.deckId);

        const gameData = {
            gameId: game.gameId,
            datePlayed,
            winnerDeckId: parseInt(winningDeck),
            playerCount: selectedPlayers.length,
            decks: deckIds.map(id => parseInt(id)),
        };

        try {
            const response = await fetchWithAuth(`http://localhost:8080/api/game/${gameId}`, {
                method: 'PUT',
                body: JSON.stringify(gameData),
            });

            if (response.ok) {
                console.log("Game updated successfully");
                navigate('/home');
            } else {
                console.error("Error updating game:", await response.text());
            }
        } catch (error) {
            console.error("Error during fetch:", error);
        }
    };

    if (!game) {
        return <div>Loading...</div>;
    }

    return (
        <form onSubmit={handleSubmit}>
            <div className="mb-3">
                <label htmlFor="datePlayed">Date Played</label>
                <input
                    type="date"
                    id="datePlayed"
                    className="form-control"
                    value={datePlayed}
                    onChange={(e) => setDatePlayed(e.target.value)}
                    required
                />
            </div>

            {selectedPlayers.map((sp, index) => (
                <div key={index}>
                    <label>Select Player {index + 1}</label>
                    <select
                        className="form-select"
                        value={sp.playerId || ''}
                        onChange={(e) => handlePlayerChange(index, e.target.value)}
                        required
                    >
                        <option value="">Select Player</option>
                        {players.map(player => (
                            <option key={player.playerId} value={player.playerId}>
                                {player.firstName} {player.lastName}
                            </option>
                        ))}
                    </select>

                    {sp.playerId && (
                        <select
                            className="form-select mt-2"
                            value={sp.deckId || ''}
                            onChange={(e) => handleDeckChange(index, e.target.value)}
                            required
                        >
                            <option value="">Select Deck</option>
                            {(() => {
                                const selectedPlayer = players.find(player => player.playerId === sp.playerId);
                                if (!selectedPlayer) {
                                    return <option value="">No player found</option>;
                                }
                                if (!selectedPlayer.playerDecks || selectedPlayer.playerDecks.length === 0) {
                                    return <option value="">No decks available</option>;
                                }
                                return selectedPlayer.playerDecks.map(deck => (
                                    <option key={deck.deckId} value={deck.deckId}>
                                        {deck.name}
                                    </option>
                                ));
                            })()}
                        </select>
                    )}
                </div>
            ))}

            {selectedDecks.length > 0 && (
                <div className="mb-3">
                    <label htmlFor="winningDeck">Winning Deck</label>
                    <select
                        id="winningDeck"
                        className="form-select"
                        value={winningDeck}
                        onChange={(e) => setWinningDeck(e.target.value)}
                        required
                    >
                        <option value="">Select Winning Deck</option>
                        {selectedDecks.map(deck => (
                            <option key={deck.deckId} value={deck.deckId}>
                                {deck.name}
                            </option>
                        ))}
                    </select>
                </div>
            )}

            <button type="submit" className="btn btn-primary mt-3">
                Submit
            </button>
        </form>
    );
}

export default EditGameForm;
