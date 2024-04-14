import Phaser from "phaser";
import React, {useEffect, useRef} from "react";
import shipImg from "../../phaser/assets/ship.png"; // Adjust the path as needed
import playerSprite from "../../phaser/assets/player.png"; // Adjust the path as needed
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import {
    PLAYER_SPRITE_HEIGHT,
    PLAYER_SPRITE_WIDTH,
    PLAYER_START_X,
    PLAYER_START_Y,
    PLAYER_HEIGHT,
    PLAYER_WIDTH,
    SHIP_WIDTH, SHIP_HEIGHT
} from "../../phaser/constants"; // Adjust the path as needed

const Game = () => {
    const stompClientRef = useRef(null)
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

            this.load.spritesheet('otherPlayer', playerSprite, {
                frameWidth: PLAYER_SPRITE_WIDTH,
                frameHeight: PLAYER_SPRITE_HEIGHT,
            });
        }

        function create() {
            this.ship = this.add.image(0, 0, 'ship');
            player.sprite = this.add.sprite(PLAYER_START_X, PLAYER_START_Y, 'player');
            player.sprite.displayHeight = PLAYER_HEIGHT;
            player.sprite.displayWidth = PLAYER_WIDTH;

            this.anims.create({
                key: 'running',
                frames: this.anims.generateFrameNumbers('player'),
                frameRate: 24,
                repeat: -1
            })

            this.input.keyboard.on('keydown', (event) => {
                if (!pressedKeys.current.includes(event.code)) {
                    pressedKeys.current.push(event.code);
                }
            });
            this.input.keyboard.on('keyup', (event) => {
                pressedKeys.current = pressedKeys.current.filter((key) => key !== event.code);
            });


            this.cursors = this.input.keyboard.createCursorKeys();
            connectWebSocket(player);
        }

        function update() {
            this.scene.scene.cameras.main.centerOn(player.sprite.x, player.sprite.y);
            movePlayer(pressedKeys.current, player.sprite)
            animateMovement(pressedKeys.current, player.sprite)
        }

        function connectWebSocket(player) {
            const socket = new SockJS('http://localhost:8080/ws');
            stompClientRef.current = Stomp.over(socket);
            stompClientRef.current.connect({}, () => {
                stompClientRef.current.subscribe('/topic/move', (message) => {
                    const playerPosition = JSON.parse(message.body);
                    updateGameState(player, playerPosition);
                });
            }, (error) => {
                console.error('WebSocket connection error:', error);
            });
        }

        function movePlayer(pressedKeys, sprite) {

            if (pressedKeys.includes('ArrowUp')) {
                sendMove('UP', sprite.flipX);
            } else if (pressedKeys.includes('ArrowDown')) {
                sendMove('DOWN', sprite.flipX);
            } else if (pressedKeys.includes('ArrowLeft')) {
                sprite.setFlipX(true);
                sendMove('LEFT', true);
            } else if (pressedKeys.includes('ArrowRight')) {
                sprite.setFlipX(false);
                sendMove('RIGHT', false);
            }
        }

        function sendMove(direction, flip) {
            console.log('jwtToken', jwtToken);
            console.log('sessionId', sessionId);
            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.send('/app/move', JSON.stringify({
                    direction: direction,
                    flip: flip,
                    token: jwtToken,
                    sessionId: sessionId
                }), {});
            }
        }

        function updateGameState(player, playerPosition) {
            console.log("New player position:", playerPosition);
            player.sprite.x = playerPosition.newPositionX;
            player.sprite.y = playerPosition.newPositionY;

            if (playerPosition.flip !== undefined) {
                player.sprite.setFlipX(playerPosition.flip)
            }
            console.log("Updated player coordinates:", player.sprite.x, player.sprite.y);
        }

        function animateMovement (keys, player){
            const runningKeys = ['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'];

            if(
                keys.some((key) => runningKeys.includes(key)) &&
                !player.anims.isPlaying
            ){
                player.play('running');
            } else if (
                !keys.some((key) => runningKeys.includes(key)) &&
                player.anims.isPlaying
            ){
                player.stop('running');
            }
        }


        return () => {
            game.destroy(true);
        };
    });

    return(
        <div id="game-container">
            <canvas/>
        </div>
    )
};

export default Game;
