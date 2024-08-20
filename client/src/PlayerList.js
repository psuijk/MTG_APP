import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { fetchWithAuth } from "./apiUtils";

function PlayerList() {

    // STATE
    const [players, setPlayers] = useState([]);
    const url = "http://localhost:8080/api/player";
    //const navigate = useNavigate();

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

    // handle delete
    const handleDeletePlayer = (playerId) => {
        const player = players.find(p => p.playerId === playerId);
        if (window.confirm(`Delete Player: username - ${player.username} / Name - ${player.firstName} ${player.lastName}?`)) {
            const init = {
                method: 'DELETE'
            };
            fetch(`${url}/${playerId}`, init)
                .then(response => {
                    if (response.status === 204) {
                        const newPlayers = players.filter(p => p.playerId !== playerId);
                        setPlayers(newPlayers);
                    } else {
                        return Promise.reject(`Unexpected Status Code: ${response.status}`)
                    }
                })
                .catch(console.log)
        }
    }


    return (<>
        <section className="container">
            <h2 className="mb-4">Players</h2>
            <Link className="btn btn-outline-secondary mb-4" to={'/player/add'}>Add A Player</Link>
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
                                <Link className="btn btn-outline-primary" to={`/player/edit/${player.playerId}`}>Edit</Link>
                                <button onClick={() => handleDeletePlayer(player.playerId)} className="btn btn-outline-danger ml-3">Delete</button>
                            </td>
                        </tr>
                    )
                    }
                </tbody>
            </table>
        </section>
    </>);
}


export default PlayerList;