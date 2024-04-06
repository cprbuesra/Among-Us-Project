import React, {useEffect, useRef, useState} from "react";
import * as SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import PlayerCircle from "./PlayerCircle";

const Game = () => {
    const [position, setPosition] = useState({ x: 1400, y: 1400 });
    const stompClient = useRef(null);


    useEffect(() => {

        const playerId = localStorage.getItem('playerId');
        if (!playerId) {
            console.error('Player ID is missing. Cannot establish WebSocket connection.');
            return;
        }

        const socket = new SockJS('http://localhost:8080/ws');
        stompClient.current = new Client({
            webSocketFactory: () => socket,
        });

        stompClient.current.activate();

        stompClient.current.onConnect = (frame) => {
            console.log('Connected: ' + frame);

            stompClient.current.subscribe('/topic/move', (message) => {
                console.log('Message received: ', message);
                const playerPosition = JSON.parse(message.body);
                console.log('Player position:', playerPosition);
                setPosition({ x: playerPosition.newPositionX, y: playerPosition.newPositionY });
            });
        };

        stompClient.current.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };

        return () => {
            stompClient.current.deactivate();
        };
    }, []);

    const sendMovement = (direction, playerId) => {
        if (stompClient.current && stompClient.current.active) {
            stompClient.current.publish({
                destination: "/app/move",
                body: JSON.stringify({ id: playerId, direction: direction }),
            });
        }
    };

    useEffect(() => {
        const handleKeyPress = async (event) => {

            const PlayerId = localStorage.getItem('playerId');

            let direction;
            switch (event.key) {
                case "ArrowUp": direction = "UP"; break;
                case "ArrowDown": direction = "DOWN"; break;
                case "ArrowLeft": direction = "LEFT"; break;
                case "ArrowRight": direction = "RIGHT"; break;
                default: return;
            }

            sendMovement(direction, PlayerId);
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, []);

    const mapStyle = {
        position: 'absolute',
        width: '200%',
        height: '200%',
        left: `calc(50% - ${position.x}px)`,
        top: `calc(50% - ${position.y}px)`,
        backgroundImage: 'url(/ship.png)',
        backgroundSize: 'contain',
    };

    return (
        <div style={{ position: 'relative', width: '100vw', height: '100vh', overflow: 'hidden' }}>
            <div style={mapStyle}></div>
            <PlayerCircle position={position}/>
        </div>
    );
};

export default Game;