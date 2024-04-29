import React, {useCallback, useEffect, useState} from 'react';
import axios from "axios";

function RoomList() {
    const [rooms, setRooms] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newRoomName, setNewRoomName] = useState('');


    const fetchRooms = useCallback(async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/gameRooms/getGameRooms');
            console.log('Rooms:', response.data);
            setRooms(response.data);
        } catch (error) {
            console.error('Error fetching rooms:', error);
            setRooms([]);
        }
    }, []);

    useEffect(() => {
        fetchRooms();
    }, [fetchRooms]);

    const handleJoinRoom = async (e, roomId) => {
        e.preventDefault();

        try {
            const response = await axios.post('http://localhost:8080/api/rooms/joinGameRoom', {
                roomId: roomId
            });

            console.log('Joined room successfully:', response.data);
            alert('Joined room successfully');
        } catch (error) {
            console.error('Error joining room:', error);
            alert('Error joining room');
        }
    };


    const refreshRooms = () => {
        fetchRooms();
    };

    const handleCreateRoom = async () => {
        if (!newRoomName) {
            alert('Please enter a room name');
            return;
        }

        try {
            const response = await axios.post(`http://localhost:8080/api/gameRooms/createGameRoom`, {
                roomName: newRoomName
            });
            console.log('Game room created:', response.data);
            setIsModalOpen(false); // Close the modal after creation
            setNewRoomName(''); // Reset the room name input
            refreshRooms(); // Refresh the list of rooms
        } catch (error) {
            console.error('Error creating game room:', error);
            alert('Error creating game room');
        }
    };




    return (
        <div>
            <h1>Join a Room</h1>
            {rooms.length === 0 && <p>No rooms available, please create a room</p>}
            <ul>
                {rooms.map(room => (
                    <p key={room.id}>
                        {room.name}
                        <button onClick={(e) => handleJoinRoom(e, room.id)}>Join</button>
                    </p>
                ))}
            </ul>
            <button onClick={refreshRooms}>Refresh</button>
            <button onClick={() => setIsModalOpen(true)}>Create Room</button>

            {isModalOpen && (
                <div style={{
                    position: 'fixed',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    backgroundColor: 'white',
                    padding: '20px',
                    zIndex: 1000
                }}>
                    <h2>Create a New Room</h2>
                    <input
                        type="text"
                        placeholder="Enter room name"
                        value={newRoomName}
                        onChange={(e) => setNewRoomName(e.target.value)}
                    />
                    <button onClick={handleCreateRoom}>Create</button>
                    <button onClick={() => setIsModalOpen(false)}>Cancel</button>
                </div>
            )}

            {isModalOpen && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    backgroundColor: 'rgba(0, 0, 0, 0.5)',
                    zIndex: 999
                }} onClick={() => setIsModalOpen(false)}></div>
            )}
        </div>
    );
}

export default RoomList;
