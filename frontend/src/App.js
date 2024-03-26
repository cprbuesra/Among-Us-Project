import React, { useState, useEffect } from 'react';
import PlayerCircle from './Components/PlayerCircle'; // Ensure this component is designed to stay in the center of the viewport
import './App.css';

function App() {
    const [viewport, setViewport] = useState({ x: 0, y: 0 }); // Represents the top-left corner of the visible area of the map

    useEffect(() => {
        const handleKeyDown = (e) => {
            const moveBy = 20; // Adjust this value for the "speed" of the movement
            switch(e.key) {
                case 'ArrowUp': setViewport(prev => ({ ...prev, y: prev.y + moveBy })); break;
                case 'ArrowDown': setViewport(prev => ({ ...prev, y: prev.y - moveBy })); break;
                case 'ArrowLeft': setViewport(prev => ({ ...prev, x: prev.x + moveBy })); break;
                case 'ArrowRight': setViewport(prev => ({ ...prev, x: prev.x - moveBy })); break;
                default: break;
            }
        };

        window.addEventListener('keydown', handleKeyDown);

        return () => window.removeEventListener('keydown', handleKeyDown);
    }, []);

    const mapStyle = {
        width: '200%', // Adjust based on your map size
        height: '200%', // Adjust based on your map size
        position: 'absolute',
        left: viewport.x + 'px',
        top: viewport.y + 'px',
        backgroundImage: 'url(/The_Skeld_map.webp)',
        backgroundSize: 'contain', // Adjust as needed to fit your map design
    };

    return (
        <div className="viewport" style={{ position: 'relative', width: '100vw', height: '100vh', overflow: 'hidden' }}>
            <div style={mapStyle}>
                <PlayerCircle position={{ x: window.innerWidth / 2, y: window.innerHeight / 2 }} /> {/* Keep the player in the center */}
            </div>
        </div>
    );
}

export default App;
