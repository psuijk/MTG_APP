import { BrowserRouter as Router } from "react-router-dom";
import { Routes, Route } from "react-router-dom";
import Home from "./Home";
import Navbar from "./Navbar";
import Login from './Login';
import Welcome from "./Welcome";
import PlayerList from "./PlayerList";
import SignUp from "./SignUp";
import PlayerDashboard from "./PlayerDashboard";
import GameList from "./GameList";

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Welcome />} />
        <Route path="/home" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/players" element={<PlayerList />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/playerDashboard" element={<PlayerDashboard />} />
        <Route path="/gameList" element={<GameList />} />
      </Routes>
    </Router>
  );
}

export default App;
