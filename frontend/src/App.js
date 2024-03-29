import './App.css';
import Home from "./home/home";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Game from "./Components/Game";

function App() {

    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home/>} />
                    <Route path="/game" element={<Game/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
