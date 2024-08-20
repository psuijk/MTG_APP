import { useEffect, useState } from "react";
import { fetchPlayer, fetchWithAuth } from "./apiUtils";

function PlayerDashboard() {
    // STATE
    const [player, setPlayer] = useState(null);
    const [games, setGames] = useState([]);

    // Retrieve the username from localStorage
    const username = localStorage.getItem('username');

    useEffect(() => {
        if (username) {
            console.log("Sending this username:", username);
            fetchPlayer(username)
                .then(player => {
                    console.log("Received player data from fetchPlayer:", player);
                    if (player) {
                        setPlayer(player);
                        fetchGames(player.playerId);
                    } else {
                        console.error("No player found in the response");
                    }
                })
                .catch(error => console.error("Error fetching player:", error));
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
            .then(data => setGames(data))
            .catch(console.log);
    };

    return (
        <section className="container">
            <h2 className="mb-4">{player?.firstName}'s Decks</h2>
            {player ? (
                <table className="table table-striped table-hover">
                    <thead className="thead-dark">
                        <tr>
                            <th>Deck Name</th>
                            <th>Commander</th>
                            <th>Active</th>
                        </tr>
                    </thead>
                    <tbody>
                        {player.playerDecks.slice(0, 10).map((deck, index) => (
                            <tr key={index}>
                                <td>{deck.name}</td>
                                <td>{deck.commanderId}</td>
                                <td>{deck.active ? 'Yes' : 'No'}</td>
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
                        <tr key={game.gameId}>
                            <td>{game.datePlayed}</td>
                            <td>{game.winnerDeckId}</td>
                            <td>{game.playerCount}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </section>
    );
}

export default PlayerDashboard;
