import React, { useState, useEffect } from 'react';
import { fetchWithAuth } from './apiUtils'; // Adjust path if needed

function GameForm() {
    const [players, setPlayers] = useState([]);
    const [playerCount, setPlayerCount] = useState(4); // Default to 4 players
    const [selectedPlayers, setSelectedPlayers] = useState([]); // [{ playerId: x, deckId: y }]
    const [winningDeck, setWinningDeck] = useState(''); // State to store the winning deck
    const [selectedDecks, setSelectedDecks] = useState([]); // Array of selected deck objects
    const [datePlayed, setDatePlayed] = useState(''); // State to store the date played

    // Fetch players from backend
    useEffect(() => {
        const fetchPlayers = async () => {
            try {
                const response = await fetchWithAuth('http://localhost:8080/api/player'); // Adjust endpoint as necessary
                const players = await response.json();
                console.log("Fetched players:", players); // Log players to ensure they are fetched
                setPlayers(players);
            } catch (error) {
                console.error("Error fetching players:", error);
            }
        };

        fetchPlayers();
    }, []);

    // Handle player selection
    const handlePlayerChange = (index, playerId) => {
        const player = players.find(p => p.playerId === parseInt(playerId)); // Ensure playerId is compared as a number
        console.log(`Selected player ${index}:`, player); // Log selected player
        const newSelectedPlayers = [...selectedPlayers];
        newSelectedPlayers[index] = { playerId: parseInt(playerId), deckId: '' }; // Ensure playerId is a number
        setSelectedPlayers(newSelectedPlayers);
        updateSelectedDecks(newSelectedPlayers);
    };

    // Handle deck selection
    const handleDeckChange = (index, deckId) => {
        const selectedPlayer = players.find(player => player.playerId === selectedPlayers[index].playerId);
        const selectedDeck = selectedPlayer?.playerDecks.find(deck => deck.deckId === parseInt(deckId));

        const newSelectedPlayers = [...selectedPlayers];
        newSelectedPlayers[index].deckId = deckId;
        setSelectedPlayers(newSelectedPlayers);
        updateSelectedDecks(newSelectedPlayers, selectedDeck);
    };

    // Update the array of selected decks
    const updateSelectedDecks = (selectedPlayers, newDeck) => {
        const decks = selectedPlayers
            .map(sp => {
                const player = players.find(p => p.playerId === sp.playerId);
                return player?.playerDecks.find(deck => deck.deckId === parseInt(sp.deckId));
            })
            .filter(deck => deck !== undefined);

        // If a new deck was selected, add it to the decks array
        if (newDeck) {
            const deckAlreadyExists = decks.find(deck => deck.deckId === newDeck.deckId);
            if (!deckAlreadyExists) {
                decks.push(newDeck);
            }
        }

        setSelectedDecks(decks);
    };

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        const deckIds = selectedPlayers.map(sp => sp.deckId);

        const gameData = {
            datePlayed,
            winnerDeckId: parseInt(winningDeck),
            playerCount,
            decks: deckIds.map(id => parseInt(id))
        };

        try {
            const response = await fetchWithAuth('http://localhost:8080/api/game', {
                method: 'POST',
                body: JSON.stringify(gameData),
            });

            if (response.ok) {
                const result = await response.json();
                console.log("Game created successfully:", result);
                // Handle successful creation (e.g., redirect or show success message)
            } else {
                console.error("Error creating game:", await response.text());
                // Handle error response
            }
        } catch (error) {
            console.error("Error during fetch:", error);
            // Handle network errors
        }
    };

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

            <div className="mb-3">
                <label htmlFor="playerCount">Player Count</label>
                <select
                    id="playerCount"
                    className="form-select"
                    value={playerCount}
                    onChange={(e) => setPlayerCount(parseInt(e.target.value))}
                    required
                >
                    <option value={4}>4 Players</option>
                    <option value={5}>5 Players</option>
                </select>
            </div>

            {[...Array(playerCount)].map((_, index) => (
                <div key={index}>
                    <label>Select Player {index + 1}</label>
                    <select
                        className="form-select"
                        value={selectedPlayers[index]?.playerId || ''}
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

                    {selectedPlayers[index]?.playerId && (
                        <select
                            className="form-select mt-2"
                            value={selectedPlayers[index]?.deckId || ''}
                            onChange={(e) => handleDeckChange(index, e.target.value)}
                            required
                        >
                            <option value="">Select Deck</option>
                            {(() => {
                                const selectedPlayer = players.find(player => player.playerId === selectedPlayers[index].playerId);
                                console.log(`Selected player for decks ${selectedPlayers[index].playerId}:`, selectedPlayer); // Log selected player
                                if (!selectedPlayer) {
                                    console.log('No player found');
                                    return <option value="">No player found</option>;
                                }
                                if (!selectedPlayer.playerDecks || selectedPlayer.playerDecks.length === 0) {
                                    console.log('No decks available');
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

export default GameForm;
