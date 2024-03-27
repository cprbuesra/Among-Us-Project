import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './home/home';
import './App.css';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" exact component={Home} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
