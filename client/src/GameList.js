import React from 'react';
import { useEffect, useState } from "react";
import { fetchWithAuth } from './apiUtils';

function GameList() {
    // STATE
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
            <h2 className="mb-4">Games</h2>
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

export default GameList;