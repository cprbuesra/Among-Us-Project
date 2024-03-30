import React, {useEffect, useRef, useState} from "react";
import * as SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import PlayerCircle from "./PlayerCircle";

const Game = () => {
    const [position, setPosition] = useState({ x: window.innerWidth / 2, y: window.innerHeight / 2 });
    const stompClient = useRef(null);



    useEffect(() => {
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
                body: JSON.stringify({ playerId, direction }),
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
        width: '500%',
        height: '500%',
        left: `calc(50% - ${position.x}px)`,
        top: `calc(50% - ${position.y}px)`,
        backgroundImage: 'url(/The_Skeld_map.webp)',
        backgroundSize: 'contain',
    };

    return (
        <div style={{ position: 'relative', width: '100vw', height: '100vh', overflow: 'hidden' }}>
            <div style={mapStyle}></div>
            <PlayerCircle />
        </div>
    );
};

export default Game;