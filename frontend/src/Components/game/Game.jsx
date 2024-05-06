import Phaser from "phaser";
import React, {useEffect, useRef} from "react";
import shipImg from "./assets/ship.png";
import playerSprite from "./assets/player.png";
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import taskImg from "./assets/task.png";
import {
    PLAYER_SPRITE_HEIGHT,
    PLAYER_SPRITE_WIDTH,
    PLAYER_START_X,
    PLAYER_START_Y,
    PLAYER_HEIGHT,
    PLAYER_WIDTH,
    TASK_POSITIONS,
} from "./constants";
import {useNavigate} from "react-router-dom";

const Game = () => {
    const stompClientRef = useRef(null)
    const jwtToken = sessionStorage.getItem('jwtToken');
    const sessionId = sessionStorage.getItem('sessionId');
    const roomId = sessionStorage.getItem('roomId');
    const player = {};
    const players = useRef(new Map());
    const pressedKeys = useRef([]);
    const navigate = useNavigate();

    useEffect(() => {

        if (!jwtToken || !sessionId) {
            navigate("/");
        }

        const config = {
            type: Phaser.WEBGL,
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
            const socket = new SockJS('http://localhost:8080/ws');
            stompClientRef.current = Stomp.over(socket);
            this.load.image('ship', shipImg);
            this.load.spritesheet('player', playerSprite, {
                frameWidth: PLAYER_SPRITE_WIDTH,
                frameHeight: PLAYER_SPRITE_HEIGHT
            });

            this.load.spritesheet('otherPlayer', playerSprite, {
                frameWidth: PLAYER_SPRITE_WIDTH,
                frameHeight: PLAYER_SPRITE_HEIGHT,
            });

            this.load.image('task', taskImg);
        }

        function create() {
            this.ship = this.add.image(0, 0, 'ship');
            player.sprite = this.add.sprite(PLAYER_START_X, PLAYER_START_Y, 'player');
            player.sprite.displayHeight = PLAYER_HEIGHT;
            player.sprite.displayWidth = PLAYER_WIDTH;

            TASK_POSITIONS.forEach((pos) => {
                const task = this.add.image(pos.x, pos.y, 'task');
                task.setScale(0.03);
                task.setInteractive();
                task.on('pointerdown', () => {
                    showTaskPopup(this, task);

                });
            });

            function showTaskPopup(scene, task) {
                const cam = scene.cameras.main;

                // Background overlay
                const bg = scene.add.graphics({ fillStyle: { color: 0x000000, alpha: 0.5 } });
                bg.fillRect(0, 0, cam.width, cam.height);
                bg.setScrollFactor(0);

                // Popup window
                const popup = scene.add.rectangle(cam.centerX, cam.centerY, 200, 150, 0xffffff);
                popup.setScrollFactor(0);

                // Task instructions or status text
                const text = scene.add.text(cam.centerX, cam.centerY - 20, 'Task Status', { fontSize: '16px', color: '#000' }).setOrigin(0.5);
                text.setScrollFactor(0);

                // Finish button
                const finishButton = scene.add.text(cam.centerX - 80, cam.centerY + 20, 'Finish', { fontSize: '18px', color: '#00ff00' }).setInteractive();
                finishButton.setScrollFactor(0);
                finishButton.on('pointerdown', () => {
                    bg.destroy();
                    popup.destroy();
                    text.destroy();
                    closeButton.destroy();
                    finishButton.destroy();
                    task.destroy();  // Removes the task from the map
                });

                // Close button
                const closeButton = scene.add.text(cam.centerX + 40, cam.centerY + 20, 'Close', { fontSize: '18px', color: '#ff0000' }).setInteractive();
                closeButton.setScrollFactor(0);
                closeButton.on('pointerdown', () => {
                    bg.destroy();
                    popup.destroy();
                    text.destroy();
                    closeButton.destroy();
                    finishButton.destroy();
                    // Task remains on the map
                });
            }



            // Tastatureingaben abfangen
            this.input.keyboard.on('keydown', (event) => {
                // Tastatureingaben bearbeiten
            });

            players.current.set(sessionId, player.sprite);

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

                // Check if the key is one of the arrow keys
                if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(event.code)) {
                    event.preventDefault();  // Prevent the default action (scrolling)
                }
            });

            this.input.keyboard.on('keyup', (event) => {
                pressedKeys.current = pressedKeys.current.filter((key) => key !== event.code);
                if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(event.code)) {
                    event.preventDefault();  // Prevent the default action (scrolling)
                }
            });


            stompClientRef.current.connect({}, () => {


                stompClientRef.current.subscribe(`/topic/move/${roomId}`, (message) => {
                    const playerPosition = JSON.parse(message.body);
                    console.log('This is the player position from the server: ' + playerPosition);
                    console.log('This is the session ID from the server: ' + playerPosition.sessionId);
                    console.log('This is the session ID from the client: ' + sessionId);
                    console.log('Position of local player: ' + player.sprite.x + ' ' + player.sprite.y);

                    if (players.current.has(playerPosition.sessionId)) {
                        let playerSprite = players.current.get(playerPosition.sessionId);
                        if (playerPosition.newPositionX < playerSprite.x) { // Moving left
                            playerSprite.setFlipX(true);
                        } else if (playerPosition.newPositionX > playerSprite.x) { // Moving right
                            playerSprite.setFlipX(false);
                        }

                        playerSprite.x = playerPosition.newPositionX;
                        playerSprite.y = playerPosition.newPositionY;
                        playerSprite.moving = true;
                    } else if (playerPosition.sessionId !== sessionId) {
                        createPlayerSprite(this, playerPosition.sessionId, playerPosition.newPositionX, playerPosition.newPositionY);
                        let newPlayerSprite = players.current.get(playerPosition.sessionId);
                        newPlayerSprite.setFlipX(playerPosition.flip);
                    }
                });
                stompClientRef.current.subscribe(`/topic/moveEnd/${roomId}`, (message) => {
                    const endMove = JSON.parse(message.body);
                    console.log('Move ended for player: ' + endMove);
                    const playerSprite = players.current.get(endMove.sessionId);
                    if (playerSprite) {
                        playerSprite.moving = false;
                    }
                });
                stompClientRef.current.subscribe('/topic/leave', (message) => {
                    const disconnectedPlayer = JSON.parse(message.body);
                    removePlayerSprite(disconnectedPlayer.sessionId);
                });
            });
        }

        function update() {
            this.scene.scene.cameras.main.centerOn(player.sprite.x, player.sprite.y);
            const playerMoved = movePlayer(pressedKeys.current, player.sprite)
            if (playerMoved) {
                player.movedLastFrame = true;
            } else {
                if (player.movedLastFrame) {
                    if (stompClientRef.current && stompClientRef.current.connected) {
                        stompClientRef.current.send('/app/moveEnd', JSON.stringify({
                            token: jwtToken,
                            sessionId: sessionId,
                            roomId: roomId
                        }), {});
                    }
                    player.movedLastFrame = false;
                }
            }
            animateMovement(pressedKeys.current, player.sprite)
            players.current.forEach((playerSprite, sessionId) => {
                if (sessionId !== sessionStorage.getItem('sessionId')) { // Don't update the local player in this loop
                    if (playerSprite.moving && !playerSprite.anims.isPlaying) {
                        playerSprite.play('running');
                    } else if (!playerSprite.moving && playerSprite.anims.isPlaying) {
                        playerSprite.stop('running');
                    }
                }
            });
        }

        function movePlayer(pressedKeys, sprite) {
            let playerMoved = false

            if (pressedKeys.includes('ArrowUp')) {
                sendMove('UP', sprite.flipX);
                playerMoved = true;
            } else if (pressedKeys.includes('ArrowDown')) {
                sendMove('DOWN', sprite.flipX);
                playerMoved = true;
            } else if (pressedKeys.includes('ArrowLeft')) {
                sprite.setFlipX(true);
                sendMove('LEFT', true);
                playerMoved = true;
            } else if (pressedKeys.includes('ArrowRight')) {
                sprite.setFlipX(false);
                sendMove('RIGHT', false);
                playerMoved = true;
            }
            return playerMoved;
        }

        function sendMove(direction, flip) {
            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.send('/app/move', JSON.stringify({
                    direction: direction,
                    flip: flip,
                    token: jwtToken,
                    sessionId: sessionId,
                    roomId: roomId
                }), {});
            }
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

        function createPlayerSprite(scene, sessionId) {
            let newPlayerSprite = scene.add.sprite(PLAYER_START_X, PLAYER_START_Y, 'player');
            newPlayerSprite.displayHeight = PLAYER_HEIGHT;
            newPlayerSprite.displayWidth = PLAYER_WIDTH;
            newPlayerSprite.moving = false;
            players.current.set(sessionId, newPlayerSprite);
        }

        function removePlayerSprite(sessionId) {
            let playerSprite = players.current.get(sessionId);
            if (playerSprite) {
                playerSprite.destroy();
                players.current.delete(sessionId);
            }
        }



        window.onbeforeunload = () => {
            sessionStorage.removeItem('jwtToken');
            sessionStorage.removeItem('sessionId');

            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.send('/app/leave', JSON.stringify({
                    token: jwtToken,
                    sessionId: sessionId
                }), {});
            }
        };


        return () => {
            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.disconnect();
            }
            game.destroy(true);
        };
    })

    return(
        <div id="game-container">
            <canvas/>
        </div>
    )
}

export default Game;
