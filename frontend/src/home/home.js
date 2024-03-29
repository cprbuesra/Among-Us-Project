import React from 'react';
import { Container } from 'react-bootstrap';
import logo from './logo.png';
import './home.css';

const Home = () => {
    return (
        <div className="container">
            <a href="/" className="home-button">&#8962;</a>

            <div className="content">
                <img src={logo} className="logo mb-4" alt="Logo"/>
                <div className="menu">
                    <a href="#profile">profile</a>
                    <a href="#howto">how to</a>
                    <a href="#about">about</a>
                </div>
                <div className="input-group">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Enter Name"
                        maxLength={10}
                        required={true}
                    />
                    <button className="btn btn-danger">play</button>
                </div>
            </div>
        </div>
    );
};

export default Home;
