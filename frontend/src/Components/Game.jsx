import React, {useEffect, useRef, useState} from "react";
import * as SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import PlayerCircle from "./PlayerCircle";

const Game = () => {
    const [position, setPosition] = useState({ x: 1400, y: 1400 });
    const [players, setPlayers] = useState([]);
    const stompClient = useRef(null);
    const [jwtToken] = useState(localStorage.getItem('jwtToken'));
    const [sessionId] = useState(localStorage.getItem('sessionId'));


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

                // Update players state
                setPlayers(prevPlayers => {
                    const existingPlayer = prevPlayers.find(player => player.sessionId === playerPosition.sessionId);
                    if (existingPlayer) {
                        // Update existing player position
                        return prevPlayers.map(player => player.sessionId === playerPosition.sessionId ? playerPosition : player);
                    } else {
                        // Add new player
                        return [...prevPlayers, playerPosition];
                    }
                });
                if (playerPosition.sessionId === sessionId) {
                    setPosition({ x: playerPosition.newPositionX, y: playerPosition.newPositionY })
                }
            });
        };

        stompClient.current.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };

        return () => {
            stompClient.current.deactivate();
        };
    }, [sessionId]);


    const sendMovement = (direction) => {
        if (stompClient.current && stompClient.current.active) {
            stompClient.current.publish({
                destination: "/app/move",
                body: JSON.stringify({
                    direction: direction,
                    token: jwtToken,
                    sessionId: sessionId
                }),
            });
        }
    };

    useEffect(() => {
        const handleKeyPress = async (event) => {

            let direction;
            switch (event.key) {
                case "ArrowUp": direction = "UP"; break;
                case "ArrowDown": direction = "DOWN"; break;
                case "ArrowLeft": direction = "LEFT"; break;
                case "ArrowRight": direction = "RIGHT"; break;
                default: return;
            }

            sendMovement(direction);
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    });

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
            {players.map(player => <PlayerCircle key={player.sessionId} position={{ x: player.newPositionX, y: player.newPositionY }} />)}
        </div>
    );
};

export default Game;