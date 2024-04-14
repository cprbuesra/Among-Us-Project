import React from "react";

const PlayerCircle = ({position}) => {
    const style = {
        position: 'absolute',
        left: `${position.x}px`,
        top: `${position.y}px`,
        width: '30px',
        height: '30px',
        borderRadius: '50%',
        backgroundColor: 'red',
        transform: 'translate(-50%, -50%)',
    };

    return <div style={style}></div>;
};

export default PlayerCircle;