import React, { useEffect, useState } from "react";
import axios from "axios"; // Make sure to import axios
import PlayerCircle from "./PlayerCircle";

const Game = () => {
    const [position, setPosition] = useState({ x: 850, y: 260 }); // Initialize with a default position

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
                    userId: 1, // Update with dynamic user ID handling
                    direction: direction
                });
                setPosition({ x: response.data.x, y: response.data.y });
            } catch (error) {
                console.error('Error moving player:', error);
            }
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, [position]); // Consider removing position from dependency array if it causes excessive API calls

    const mapStyle = {
        position: 'absolute',
        width: '200%', // Adjust based on your map's size
        height: '200%', // Adjust based on your map's size
        left: `calc(50% - ${position.x}px)`,
        top: `calc(50% - ${position.y}px)`,
        backgroundImage: 'url(/The_Skeld_map.webp)', // Your map image
        backgroundSize: 'contain', // or 'cover', depending on your preference
    };

    return (
        <div style={{position: 'relative', width: '100vw', height: '100vh', overflow: 'hidden'}}>
            <div style={mapStyle}>
                {/* The map moves, but the PlayerCircle stays at the center */}
            </div>
            <PlayerCircle position={position}/>
        </div>
    );
};

export default Game;
