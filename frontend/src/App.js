import React, { useState, useEffect } from 'react';
import PlayerCircle from './Components/PlayerCircle'; // Stelle sicher, dass dieser Komponentenpfad korrekt ist
import './App.css';

function App() {
    const [viewport, setViewport] = useState({ x: 0, y: 0 }); // Stellt den linken oberen Eckpunkt des sichtbaren Bereichs der Karte dar

    useEffect(() => {
        const handleKeyDown = (e) => {
            const moveBy = 5;
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

    const playerPosition = {
        x: window.innerWidth / 2,
        y: window.innerHeight  / 2
    };



    const mapStyle = {
        width: '500%', // Passe basierend auf der Größe deiner Karte an
        height: '500%', // Passe basierend auf der Größe deiner Karte an
        position: 'absolute',
        left: viewport.x + 'px',
        top: viewport.y + 'px',
        backgroundImage: 'url(/The_Skeld_map.webp)',
        backgroundSize: 'contain', // Passe bei Bedarf an, um dein Karten-Design anzupassen
    };

    return (
        <div className="viewport" style={{ position: 'relative', width: '100vw', height: '100vh', overflow: 'hidden' }}>
            <div style={mapStyle}>
                <PlayerCircle position={playerPosition} /> {/* Halte den Spielerkreis in der Mitte */}
            </div>
        </div>
    );
}

function disableZoom() {
    // Firefox
    window.addEventListener('DOMMouseScroll', preventZoom, false);
    // Chrome, Safari, Opera
    window.addEventListener('wheel', preventZoom, { passive: false });
    // Internet Explorer
    window.addEventListener('mousewheel', preventZoom, { passive: false });
}

function preventZoom(event) {
    if (event.ctrlKey === true || event.metaKey === true) {
        event.preventDefault();
    }
}

disableZoom();


export default App;
