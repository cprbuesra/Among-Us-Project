import React, {useState} from 'react';
import logo from './logo.png';
import './home.css';
import {useNavigate} from "react-router-dom";

const Home = () => {
    const [name, setName] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        setName(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
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
                navigate("/game");
            } else if (response.status === 404) {
                setError('Player with this username already exists. \nPlease enter another username.');
            } else {
                const errorData = await response.text();
                setError(errorData);
                console.error('Failed to send name to the API');
            }
        } catch (error) {
            setError('Error submitting name');
            console.error('Error submitting name:', error);
        }
    };

    return (
        <div className="container">
            <a href="/" className="home-button">&#8962;</a>

            <div className="content">
                <img src={logo} className="logo mb-4" alt="Logo"/>
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
                    <button className="btn btn-danger" >play</button>
                </form>
                {error && (
                    <div className="error-message">
                        {error.split('\n').map((line, index) => (
                            <span key={index}>{line}<br/></span>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Home;