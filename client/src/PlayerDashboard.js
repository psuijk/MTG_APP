import React, { useEffect, useState } from 'react';
import { fetchPlayer, fetchWithAuth } from './apiUtils';
import './PlayerDashboard.css';
import { useNavigate } from 'react-router-dom';

function PlayerDashboard() {
    const [player, setPlayer] = useState(null);
    const [games, setGames] = useState([]);
    const [decksWithWinrate, setDecksWithWinrate] = useState([]);
    const [deckNames, setDeckNames] = useState({}); // State to store deck names

    const username = localStorage.getItem('username');
    const navigate = useNavigate();

    useEffect(() => {
        if (username) {
            fetchPlayer(username)
                .then(player => {
                    if (player) {
                        setPlayer(player);
                        fetchGames(player.playerId);
                        fetchDecksWinrates(player.playerDecks); // Fetch winrates for all decks
                    } else {
                        console.error('No player found in the response');
                    }
                })
                .catch(error => console.error('Error fetching player:', error));
        } else {
            console.log('No username found in localStorage');
        }
    }, [username]);

    const fetchGames = (playerId) => {
        const url = `http://localhost:8080/api/game/playersGames/${playerId}`;
        fetchWithAuth(url)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .then(data => {
                setGames(data);
                // Fetch deck names for the games
                const allDeckIds = new Set();
                data.forEach(game => {
                    allDeckIds.add(game.winnerDeckId);
                    game.decks.forEach(deckId => allDeckIds.add(deckId));
                });
                fetchDeckNames(Array.from(allDeckIds));
            })
            .catch(console.log);
    };

    const fetchDeckNames = (deckIds) => {
        const fetchDeckName = async (deckId) => {
            const url = `http://localhost:8080/api/deck/${deckId}`;
            try {
                const response = await fetchWithAuth(url);
                if (response.status === 200) {
                    const deck = await response.json();
                    return { deckId, name: deck.name };
                } else {
                    console.error(`Error fetching deck ${deckId}: ${response.status}`);
                }
            } catch (error) {
                console.error(`Error fetching deck ${deckId}:`, error);
            }
            return { deckId, name: 'Unknown' };
        };

        Promise.all(deckIds.map(fetchDeckName))
            .then(results => {
                const nameMap = results.reduce((acc, { deckId, name }) => {
                    acc[deckId] = name;
                    return acc;
                }, {});
                setDeckNames(nameMap);
            });
    };

    const fetchDecksWinrates = (decks) => {
        const fetchDeckWinrate = async (deckId) => {
            const url = `http://localhost:8080/api/game/decksGames/${deckId}`;
            try {
                const response = await fetchWithAuth(url);
                if (response.status === 200) {
                    const gamesForDeck = await response.json();
                    const totalGames = gamesForDeck.length;
                    const wins = gamesForDeck.filter(game => game.winnerDeckId === deckId).length;
                    return { deckId, winrate: (totalGames > 0) ? (wins / totalGames) * 100 : 0 };
                } else {
                    console.error(`Error fetching games for deck ${deckId}: ${response.status}`);
                }
            } catch (error) {
                console.error(`Error fetching games for deck ${deckId}:`, error);
            }
            return { deckId, winrate: 0 };
        };

        Promise.all(decks.map(deck => fetchDeckWinrate(deck.deckId)))
            .then(results => {
                const winrateMap = results.reduce((acc, { deckId, winrate }) => {
                    acc[deckId] = winrate;
                    return acc;
                }, {});
                setDecksWithWinrate(winrateMap);
            });
    };

    return (
        <section className="container">
            <h2 className="mb-4">{player?.firstName}'s Decks</h2>

            <button className="btn btn-primary mb-3" onClick={() => navigate('/deck/add')}>
                Add Deck
            </button>

            {player ? (
                <table className="table table-striped table-hover">
                    <thead className="thead-dark">
                        <tr>
                            <th>Deck Name</th>
                            <th>Commander</th>
                            <th>Active</th>
                            <th>Winrate (%)</th> {/* Added column for winrate */}
                        </tr>
                    </thead>
                    <tbody>
                        {player.playerDecks.slice(0, 10).map(deck => (
                            <tr key={deck.deckId}> {/* Changed from index to deckId */}
                                <td>{deck.name}</td>
                                <td>{deck.commanderId}</td>
                                <td>{deck.active ? 'Yes' : 'No'}</td>
                                <td>{decksWithWinrate[deck.deckId] ? decksWithWinrate[deck.deckId].toFixed(2) : 'Calculating...'}</td> {/* Display winrate */}
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                <p>Loading player data...</p>
            )}

            <h2 className="mb-4 mt-5">{player?.firstName}'s Games</h2>
            <table className="table table-striped table-hover">
                <thead className="thead-dark">
                    <tr>
                        <th>Date</th>
                        <th>Winner</th>
                        <th># players</th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    {games.slice(0, 10).map(game => (
                        <tr key={game.gameId}> {/* Changed from index to gameId */}
                            <td>{game.datePlayed}</td>
                            <td>{deckNames[game.winnerDeckId] || 'Loading...'}</td> {/* Display winning deck name */}
                            <td>{game.playerCount}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </section>
    );
}

export default PlayerDashboard;
