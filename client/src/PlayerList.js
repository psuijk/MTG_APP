import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchWithAuth } from "./apiUtils";
import './PlayerList.css'; // Ensure this file exists for button styling

function PlayerList() {

    // STATE
    const [players, setPlayers] = useState([]);
    const url = "http://localhost:8080/api/player";
    const navigate = useNavigate(); // For navigation

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
            .then(data => setPlayers(data))
            .catch(console.log);
    }, []); // render once when the component loads

    const handleDeletePlayer = (playerId) => {
        const player = players.find(p => p.playerId === playerId);
        if (window.confirm(`Delete Player: username - ${player.username} / Name - ${player.firstName} ${player.lastName}?`)) {
            fetchWithAuth(`${url}`, {
                method: 'DELETE',
                body: JSON.stringify({
                    playerId: player.playerId, // Ensure fields match what backend expects
                    username: player.username,
                    firstName: player.firstName,
                    lastName: player.lastName
                }),
            })
                .then(response => {
                    return response.text().then(text => {
                        if (response.status === 204) {
                            const newPlayers = players.filter(p => p.playerId !== playerId);
                            setPlayers(newPlayers);
                        } else {
                            console.error(`Unexpected Status Code: ${response.status}`);
                            console.error('Response Text:', text);
                            return Promise.reject(`Unexpected Status Code: ${response.status}`);
                        }
                    });
                })
                .catch(console.log);
        }
    }



    // handle edit navigation
    const handleEditPlayer = (playerId) => {
        navigate(`/player/edit/${playerId}`);
    }

    return (
        <section className="container">
            <h2 className="mb-4">Players</h2>
            <button className="btn btn-outline-secondary mb-4" onClick={() => navigate('/player/add')}>Add A Player</button>
            <table className="table table-striped table-hover">
                <thead className="thead-dark">
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Username</th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    {players.map(player =>
                        <tr key={player.playerId}>
                            <td>{player.firstName}</td>
                            <td>{player.lastName}</td>
                            <td>{player.username}</td>
                            <td>
                                <button onClick={() => handleEditPlayer(player.playerId)} className="btn btn-outline-primary ml-3">Edit</button>
                                <button onClick={() => handleDeletePlayer(player.playerId)} className="btn btn-outline-danger ml-3">Delete</button>
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
        </section>
    );
}

export default PlayerList;
