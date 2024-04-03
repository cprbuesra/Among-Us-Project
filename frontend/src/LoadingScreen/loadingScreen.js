import React, { useState, useEffect } from 'react';
import {useNavigate} from 'react-router-dom';
import './LoadingScreen.css';


function LoadingScreen() {
    const [position, setPosition] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        const interval = setInterval(() => {
            setPosition(prevPosition => (prevPosition + 2) % window.innerWidth);
        }, 16); // Aktualisierung alle 16ms (entspricht ca. 60fps)

        // Simulation einer längeren Ladezeit
        setTimeout(() => {
            clearInterval(interval);
            navigate('/game'); // Weiterleitung zur Spieloberfläche nach dem Laden
        }, 3000); // Hier kannst du die Dauer des Ladebildschirms einstellen (z.B. 3000ms = 3 Sekunden)

        return () => clearInterval(interval);
    }, []);

    return (
        <div className="among-us-loading-screen">
            <div className="loading-text">
                Loading...
            </div>
        </div>
    );
}

export default LoadingScreen;
