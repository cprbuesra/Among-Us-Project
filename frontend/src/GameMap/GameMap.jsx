import React from 'react';
import mapImage from "./The_Skeld_map.png"



const GameMap = () => {
    return (
        <div style={{ position: 'relative' }}>
            <img src={mapImage} alt="Game Map" style={{ maxWidth: '100%', height: 'auto' }} />
            {/* Additional elements like player icons can be absolutely positioned over the map here */}
        </div>
    );
};

export default GameMap;
