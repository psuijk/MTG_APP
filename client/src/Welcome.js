import React from 'react';
import { Link } from 'react-router-dom';

function Welcome() {
    return (<>
        <div className="container">
            <div className="blue-box">
                <header>
                    <h1>Welcome to the MTG Gametracker App</h1>
                    <Link to="/Login">
                        <button className="btn btn-primary">Login</button>
                    </Link>
                    <Link to="/SignUp">
                        <button className="btn btn-primary">Sign Up</button>
                    </Link>
                </header>
            </div>
            <footer>
                <p>Copyright 2024</p>
            </footer>
        </div>
    </>);
}

export default Welcome;