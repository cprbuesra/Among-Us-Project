import React, { useState } from 'react';
import logo from './logo.png';
import icon from './homeicon.png';
import './home.css';
import { useNavigate } from 'react-router-dom';
import AmongUsLoadingScreen from "../LoadingScreen/AmongUsLoadingScreen";

const Home = () => {
    const [name, setName] = useState('');
    const [loading, setLoading] = useState(false); // Zustand für den Ladebildschirm
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        setName(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true); // Setze Ladezustand auf true
        try {
            const response = await fetch('http://localhost:8080/player/save', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username: name }),
            });
            if (response.ok) {
                const data = await response.json();
                console.log(data);
                localStorage.setItem('playerId', data.id);
                navigate("/LoadingScreen");
            } else {
                console.error('Failed to send name to the API');
            }
        } catch (error) {
            console.error('Error submitting name:', error);
        } finally {
            setLoading(false); // Setze Ladezustand auf false nach Abschluss des Vorgangs
        }
    };

    return (
        <div className="container">
            <a href="/" className="home-button">
                <img src={icon} alt="Home" style={{ width: '43px', height: '40px' }} />
            </a>

            <div className="content">
                <img src={logo} className="logo mb-4" alt="Logo" />
                <div className="menu">
                    <a href="/">profile</a>
                    <a href="/">how to</a>
                    <a href="/">about</a>
                </div>
                <form className="input-group" onSubmit={handleSubmit}>
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Enter Name"
                        maxLength={10}
                        required={true}
                        value={name}
                        onChange={handleInputChange}
                    />
                    <button className="btn btn-danger" type="submit">play</button> {/* Änderung: Füge den type-Attribut hinzu */}
                </form>
            </div>
            {/* Zeige den Ladebildschirm, wenn der Ladezustand true ist */}
            {loading && <AmongUsLoadingScreen />}
        </div>
    );
};

export default Home;
