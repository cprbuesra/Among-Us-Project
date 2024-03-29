import React, { useEffect, useState } from "react";
import axios from "axios";
import PlayerCircle from "./PlayerCircle";

const Game = () => {
    const [position, setPosition] = useState({ x: window.innerWidth / 2, y: window.innerHeight / 2 });

    useEffect(() => {
        const handleKeyPress = async (event) => {
            let direction;
            switch (event.key) {
                case "ArrowUp": direction = "UP"; break;
                case "ArrowDown": direction = "DOWN"; break;
                case "ArrowLeft": direction = "LEFT"; break;
                case "ArrowRight": direction = "RIGHT"; break;
                default: return;
            }

            try {
                const response = await axios.post('http://localhost:8080/user/move', {
                    userId: 1,
                    direction: direction
                });
                setPosition({ x: response.data.x, y: response.data.y });
            } catch (error) {
                console.error('Error moving player:', error);
            }
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, []);

    const mapStyle = {
        position: 'absolute',
        width: '500%',
        height: '500%',
        left: `calc(50% - ${position.x}px)`,
        top: `calc(50% - ${position.y}px)`,
        backgroundImage: 'url(/The_Skeld_map.webp)',
        backgroundSize: 'contain',
    };

    return (
        <div style={{ position: 'relative', width: '100vw', height: '100vh', overflow: 'hidden' }}>
            <div style={mapStyle}></div>
            <PlayerCircle />
        </div>
    );
};

export default Game;