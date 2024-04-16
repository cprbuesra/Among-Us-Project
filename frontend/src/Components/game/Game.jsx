import Phaser from "phaser";
import React, {useEffect, useRef} from "react";
import shipImg from "./assets/ship.png";
import playerSprite from "./assets/player.png";
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import {
    PLAYER_SPRITE_HEIGHT,
    PLAYER_SPRITE_WIDTH,
    PLAYER_START_X,
    PLAYER_START_Y,
    PLAYER_HEIGHT,
    PLAYER_WIDTH,
} from "./constants";

const Game = () => {
    const stompClientRef = useRef(null)
    const jwtToken = sessionStorage.getItem('jwtToken');
    const sessionId = sessionStorage.getItem('sessionId');
    const player = {};
    const players = useRef(new Map());
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


        }

        function create() {
            this.ship = this.add.image(0, 0, 'ship');
            player.sprite = this.add.sprite(PLAYER_START_X, PLAYER_START_Y, 'player');
            player.sprite.displayHeight = PLAYER_HEIGHT;
            player.sprite.displayWidth = PLAYER_WIDTH;

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
            });
            this.input.keyboard.on('keyup', (event) => {
                pressedKeys.current = pressedKeys.current.filter((key) => key !== event.code);
            });


            stompClientRef.current.connect({}, () => {
                stompClientRef.current.subscribe('/topic/move', (message) => {
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
                stompClientRef.current.subscribe('/topic/moveEnd', (message) => {
                    const endMove = JSON.parse(message.body);
                    const playerSprite = players.current.get(endMove.sessionId);
                    if (playerSprite) {
                        playerSprite.moving = false;
                    }
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
                            sessionId: sessionId
                        }), {});
                    }
                    player.movedLastFrame = false;
                }
            }
            animateMovement(pressedKeys.current, player.sprite)
            // Animate other players
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
                    sessionId: sessionId
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
            // Create a new sprite for the player
            let newPlayerSprite = scene.add.sprite(PLAYER_START_X, PLAYER_START_Y, 'player');
            newPlayerSprite.displayHeight = PLAYER_HEIGHT;
            newPlayerSprite.displayWidth = PLAYER_WIDTH;
            newPlayerSprite.moving = false;
            players.current.set(sessionId, newPlayerSprite);
        }

        function removePlayerSprite(sessionId) {
            let playerSprite = players.current.get(sessionId);
            if (playerSprite) {
                playerSprite.destroy();  // Remove the sprite from the game
                players.current.delete(sessionId);
            }
        }


        return () => {
            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.disconnect();
            }
            game.destroy(true);
        };
    })

    return(
        <div id="game-container">
        </div>
    )
}

export default Game;
