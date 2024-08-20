import React from 'react';
import { useEffect, useState } from 'react';
import { fetchWithAuth } from "./apiUtils";



function Home() {

    const [games, setGames] = useState([]);

    // Construct the URL using the username
    const url = `http://localhost:8080/api/game`;

    // useEffect 
    useEffect(() => {
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
    }, [url]); // run effect when url changes

    return (
        <section className="container">
            <h2 className="mb-4">Welcome to the MTG Gametracker</h2>
            <h1 className="mb-2">Recent Games</h1>
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
                    {games.map(game => (
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

export default Home;