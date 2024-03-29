import React, { useState, useEffect } from 'react';
import PlayerCircle from './Components/PlayerCircle'; // Stellen Sie sicher, dass diese Komponente so konzipiert ist, dass sie sich im Zentrum des Viewports befindet
import './App.css';
import Home from "./home/home";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

function App() {
    const [viewport, setViewport] = useState({ x: 0, y: 0 }); // Stellt die Position des oberen linken Ecks des sichtbaren Bereichs der Karte dar

    useEffect(() => {
        const handleKeyDown = (e) => {
            const moveBy = 20; // Passen Sie diesen Wert für die "Geschwindigkeit" der Bewegung an
            switch (e.key) {
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
        width: '200%', // Passen Sie dies an Ihre Karten Größe an
        height: '200%', // Passen Sie dies an Ihre Karten Größe an
        position: 'absolute',
        left: viewport.x + 'px',
        top: viewport.y + 'px',
        backgroundImage: 'url(/The_Skeld_map.webp)',
        backgroundSize: 'contain', // Passen Sie dies an Ihre Kartendesign an
    };

    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
