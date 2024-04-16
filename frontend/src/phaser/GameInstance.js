import Phaser from "phaser";
import React, { useEffect, useRef } from "react";
import shipImg from "../../phaser/assets/ship.png";  // Adjust the path as needed
import playerSprite from "../../phaser/assets/player.png";  // Adjust the path as needed
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

import {
    PLAYER_SPRITE_HEIGHT,
    PLAYER_SPRITE_WIDTH,
    PLAYER_START_X,
    PLAYER_START_Y,
    PLAYER_HEIGHT,
    PLAYER_WIDTH,
} from "../../phaser/constants";  // Adjust the path as needed

const Game = () => {
    const stompClientRef = useRef(null);
    const jwtToken = localStorage.getItem('jwtToken');
    const sessionId = localStorage.getItem('sessionId');
    const player = {};
    const pressedKeys = useRef([]);

    useEffect(() => {
        const config = {
            type: Phaser.AUTO,
            width: window.innerWidth,
            height: window.innerHeight,
            parent: 'game-container',
            physics: {
                default: 'arcade',
                arcade: {
                    gravity: { y: 0 },
                    debug: false
                }
            },
            scene: {
                preload: preload,
                create: create,
                update: update
            }
        };

        const game = new Phaser.Game(config);

        function preload() {
            this.load.image('ship', shipImg);
            this.load.spritesheet('player', playerSprite, {
                frameWidth: PLAYER_SPRITE_WIDTH,
                frameHeight: PLAYER_SPRITE_HEIGHT
            });
            this.load.image('task', 'path/to/task.png');  // Load the task icon
        }

        function create() {
            this.ship = this.add.image(0, 0, 'ship').setOrigin(0, 0);
            player.sprite = this.add.sprite(PLAYER_START_X, PLAYER_START_Y, 'player');
            player.sprite.displayHeight = PLAYER_HEIGHT;
            player.sprite.displayWidth = PLAYER_WIDTH;

            // Adding a task sprite
            const taskSprite = this.add.sprite(300, 300, 'task').setInteractive(); // Adjust x, y as needed
            taskSprite.on('pointerdown', () => onTaskClicked('task1'));

            this.input.keyboard.on('keydown', (event) => {
                if (!pressedKeys.current.includes(event.code)) {
                    pressedKeys.current.push(event.code);
                }
            });

            this.input.keyboard.on('keyup', (event) => {
                pressedKeys.current = pressedKeys.current.filter((key) => key !== event.code);
            });

            this.cursors = this.input.keyboard.createCursorKeys();
            connectWebSocket();
        }

        function update() {
            this.scene.scene.cameras.main.centerOn(player.sprite.x, player.sprite.y);
            movePlayer();
        }

        function connectWebSocket() {
            const socket = new SockJS('http://localhost:8080/ws');
            stompClientRef.current = Stomp.over(socket);
            stompClientRef.current.connect({}, () => {
                stompClientRef.current.subscribe('/topic/move', (message) => {
                    const playerPosition = JSON.parse(message.body);
                    updateGameState(playerPosition);
                });
            }, (error) => {
                console.error('WebSocket connection error:', error);
            });
        }

        function movePlayer() {
            if (pressedKeys.current.includes('ArrowUp')) {
                player.sprite.y -= 10;
            } else if (pressedKeys.current.includes('ArrowDown')) {
                player.sprite.y += 10;
            }

            if (pressedKeys.current.includes('ArrowLeft')) {
                player.sprite.x -= 10;
            } else if (pressedKeys.current.includes('ArrowRight')) {
                player.sprite.x += 10;
            }
        }

        function updateGameState(playerPosition) {
            player.sprite.x = playerPosition.newPositionX;
            player.sprite.y = playerPosition.newPositionY;
        }

        function onTaskClicked(taskKey) {
            console.log(`Task ${taskKey} clicked`);
            alert(`You clicked on task at ${taskKey}`);  // Simple alert for demonstration
        }

        return () => {
            game.destroy(true);
        };
    }, []);

    return (
        <div id="game-container">
            <canvas />
        </div>
    );
};

export default Game;
