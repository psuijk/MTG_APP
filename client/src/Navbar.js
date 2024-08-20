import { Link, useNavigate } from "react-router-dom";

function Navbar() {
    // Check if the user is signed in by verifying the presence of an auth token in localStorage
    const isLoggedIn = !!localStorage.getItem('authToken');
    const navigate = useNavigate();

    // Handle logout
    const handleLogout = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('username'); // Assuming you store the username as well
        navigate('/'); // Redirect to the home page after logout
    };

    return (
        <nav className="navbar">
            <div className="nav-links">
                <Link to={'/home'}>Home</Link>
                {isLoggedIn && <Link to={'/playerDashboard'}>Dashboard</Link>}
                {isLoggedIn && <Link to={'/players'}>Players</Link>}
                {isLoggedIn && <Link to={'/gameForm'}>Add Game</Link>}
            </div>
            {isLoggedIn && (
                <button className="btn-logout" onClick={handleLogout}>Logout</button>
            )}
        </nav>
    );
}

export default Navbar;
