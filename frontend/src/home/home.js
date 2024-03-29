import React, {useState} from 'react';
import logo from './logo.png';
import './home.css';
import {useNavigate} from "react-router-dom";


    const Home = () => {
        // State to store the input value
        const [name, setName] = useState('');
        const navigate = useNavigate();

        // Handler to update state with input value
        const handleInputChange = (e) => {
            setName(e.target.value);
        };

        // Handler for submitting the data
        const handleSubmit = async (e) => {
            e.preventDefault(); // Prevents the default form submission behavior
            try {
                // Replace 'YOUR_API_ENDPOINT' with your actual endpoint URL
                const response = await fetch('http://localhost:8080/user/save', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ username: name }), // Sending the name in the body
                });
                if (response.ok) {
                    const data = await response.json();
                    console.log(data);
                    navigate("/game");
                    // Handle success (e.g., navigate to another page or show a success message)
                } else {
                    // Handle errors (e.g., show an error message)
                    console.error('Failed to send name to the API');
                }
            } catch (error) {
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
            </div>
        </div>
    );
};

export default Home;
