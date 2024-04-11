import React, {useState} from 'react';
import logo from './logo.png';
import icon from './homeicon.png';
import './home.css';
import backgroundImage from './homebackground2.jpg'
import {useNavigate} from "react-router-dom";
import axios from "axios";

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
            const response = await axios.post('http://localhost:8080/player/save', {
                username: name
            });

            console.log('Joined successfully: ', response.data);
            localStorage.setItem('jwtToken', response.data.token);
            localStorage.setItem('sessionId', response.data.sessionId);

            if (response.data.playersCount >= 10) {
                alert('The lobby is already full.');
            } else {
                navigate("/lobby", { state: { inputName: name }});
            }
        } catch (error) {
        if (error.response && error.response.data) {
            console.log('Error submitting name:', error.response.data);
            if (error.response.status === 409) { // 409 steht normalerweise für Konflikt (Username bereits vorhanden)
                alert('This username is already taken. Please choose another one.');
            }
        } else {
            console.error('Error submitting name:', error);
            alert('This username is already taken. Please choose another one.');
        }
    }

}



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
