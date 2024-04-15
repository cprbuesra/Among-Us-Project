import Phaser from "phaser";
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import shipImg from './assets/ship.png';
import playerSprite from './assets/player.png';
import {PLAYER_SPRITE_HEIGHT, PLAYER_SPRITE_WIDTH, PLAYER_START_X, PLAYER_START_Y} from "./constants";


export class GameInstance extends Phaser.Scene {
    constructor() {
        super({key: 'GameInstance'});
        console.log("GameInstance scene initialized");
        this.player = null;
        this.cursors = null;
        this.stompClient = null;
        }



    preload() {
        console.log("Preloading assets...");
        this.load.image('ship', shipImg);
        this.load.spritesheet('player', playerSprite, {
            frameWidth: PLAYER_SPRITE_WIDTH,
            frameHeight: PLAYER_SPRITE_HEIGHT
        });
    }

    create() {
        console.log("Creating scene...");
        this.ship = this.add.image(0, 0, 'ship');
        this.player.sprite = this.add.sprite(PLAYER_START_X, PLAYER_START_Y, 'player');
        this.cursors = this.input.keyboard.createCursorKeys();
        this.connectWebSocket();
    }

    update() {
        this.scene.scene.cameras.main.centerOn(this.player.sprite.x, this.player.sprite.y);

        if (this.cursors.left.isDown) {
            this.sendMove('LEFT');
        } else if (this.cursors.right.isDown) {
            this.sendMove('RIGHT');
        }

        if (this.cursors.up.isDown) {
            this.sendMove('UP');
        } else if (this.cursors.down.isDown) {
            this.sendMove('DOWN');
        }

    }

    connectWebSocket() {
        const socket = new SockJS('http://localhost:8080/ws'); // Adjust URL to match your server
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, () => {
            this.stompClient.subscribe('/topic/move', (message) => {
                // Handle incoming message
                const playerPosition = JSON.parse(message.body);
                this.updateGameState(playerPosition);
            });
        }, (error) => {
            console.error('WebSocket connection error:', error);
        });
    }

    sendMove(direction) {

        const jwtToken = localStorage.getItem('jwtToken');
        const sessionId = localStorage.getItem('sessionId');
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.send('/app/move', JSON.stringify({
                direction: direction,
                token: jwtToken,
                sessionId: sessionId
            }), {});
        }
    }

    updateGameState(gameState) {
        // Update your game state based on server messages
        // For example, moving player sprites, updating scores, etc.
        console.log('Game state updated:', gameState);
    }


}


export default GameInstance;

