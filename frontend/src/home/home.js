import React from 'react';
import { Container } from 'react-bootstrap';
import logo from './logo.png';
import './home.css';

const Home = () => {
    return (
        <div className="vh-100 d-flex justify-content-center align-items-center">
            <img src={logo} className="logo" alt="Logo" />
        </div>
    );
};

export default Home;
