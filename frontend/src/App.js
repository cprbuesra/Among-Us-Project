import './App.css';
import Home from "./Components/home/Home";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Game from "./Components/game/Game";
import LoadingScreen from "./Components/loadingScreen/loadingScreen";

function App() {

    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home/>} />
                    <Route path="/game" element={<Game/>}/>
                    <Route path="/loadingScreen" element={<LoadingScreen/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
