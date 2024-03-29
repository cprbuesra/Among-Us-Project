import React from "react";

const PlayerCircle = () => {
    const style = {
        width: '40px',
        height: '40px',
        borderRadius: '50%',
        backgroundColor: 'red',
        position: 'fixed',
        left: '50%',
        top: '50%',
        transform: 'translate(-50%, -50%)',
    };

    return <div style={style}></div>;
};

export default PlayerCircle;