import React, { useState, useEffect } from 'react';
import './lobby.css';
import axios from 'axios';
import { useNavigate, useLocation } from "react-router-dom";
import crew from "../lobby/crew.png";

const Lobby = () => {
    const [players, setPlayers] = useState([]);
    const [inputName, setInputName] = useState('');
    const navigate = useNavigate();
    const location = useLocation();

    const fetchPlayers = async () => {
        try {
            const response = await axios.get('http://localhost:8080/player/list');
            setPlayers(response.data.players);
        } catch (error) {
            console.error('Error fetching players:', error);
        }
    };

    useEffect(() => {
        fetchPlayers();
    }, []);

    useEffect(() => {
        if (location.state && location.state.inputName) {
            const newName = location.state.inputName;
            if (!players.some(player => player.username === newName)) {
                setInputName(newName);
                setPlayers(prevPlayers => [...prevPlayers, { username: newName }]);
            }
        }
    }, [location.state, players]);


    const handleStartGame = async () => {
        navigate("/game");
    };

    return (
        <div id="lobby-body">
            <div id="lobby-container">
                <div className="content">
                    <img src={crew} className="crew mb-4" alt="Crew" />
                    <div id="player-list">
                        {players.map((player, index) => (
                            <div key={index} className="player-name">{player.username}</div>
                        ))}
                        {inputName && <div className="player-name">{inputName}</div>}
                    </div>
                </div>
                <div id="game-info">
                    <div id="player-count">
                        <span>{players.length + (inputName ? 1 : 0)}/10</span>
                    </div>
                    <div id="player-load">
                        <span></span>
                    </div>
                    <button
                        id="start-button"
                        onClick={handleStartGame}
                        //disabled={players.length + (inputName ? 1 : 0) < 5}
                    >
                        START
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Lobby;
