import React, {useState} from 'react';
import logo from './logo.png';
import icon from './homeicon.png';
import './home.css';
import backgroundImage from './homebackground2.jpg'
import {useNavigate} from "react-router-dom";


const Home = () => {
    const [name, setName] = useState('');
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const value = e.target.value;
        const regex = /^[A-Za-z]+$/;

        if (value === '' || regex.test(value)) {
            setName(value);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/player/save', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({username: name}),
            });
            if (response.ok) {
                const data = await response.json();
                console.log(data);
                localStorage.setItem('playerId', data.id);
                navigate("/game");
            } else {
                console.error('Failed to send name to the API');
            }
        } catch (error) {
            console.error('Error submitting name:', error);
        }
    };

    return (
        <div className="container" style={{
            backgroundImage: `url(${backgroundImage})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            height: '100vh',
            overflow: 'hidden'
        }}>
            <a href="/" className="home-button">
                <img src={icon} alt="Home" style={{width: '43px', height: '40px'}}/>
            </a>

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
                    <button className="btn btn-danger">play</button>
                </form>
            </div>
        </div>
    );
};

export default Home;
