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
import GameForm from "./GameForm";
import EditGame from "./EditGame";
import PlayerForm from "./PlayerForm";
import DeckForm from "./DeckForm";

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
        <Route path="/gameForm" element={<GameForm />} />
        <Route path="/editGame/:gameId" element={<EditGame />} />
        <Route path="/player/add" element={<PlayerForm />} />
        <Route path="/player/add" element={<PlayerForm />} />
        <Route path="/player/edit/:playerId" element={<PlayerForm />} />
        <Route path="/deck/add" element={<DeckForm />} />

      </Routes>
    </Router>
  );
}

export default App;
