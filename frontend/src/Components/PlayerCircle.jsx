import React from 'react';

const PlayerCircle = ({ position }) => {
    const style = {

        width: '40px',
        height: '40px',
        borderRadius: '50%',
        backgroundColor: 'red',
        position: 'fixed',
        left: position.x,
        top: position.y,

    };

    return <div style={style}></div>;
};


export default PlayerCircle;
