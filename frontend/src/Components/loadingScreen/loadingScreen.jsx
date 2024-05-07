import {useLocation, useNavigate} from 'react-router-dom';
import './LoadingScreen.css';


function LoadingScreen() {
    const location = useLocation();
    const username = location.state?.username; // retrieve username passed from Home.jsx
    const navigate = useNavigate();

    setTimeout(() => {
        navigate("/game", { state: { username: username } });
    }, 5000);


    return (
        <div className="among-us-loading-screen">
            <div className="loading-text"></div>
            <div className="runner"></div>
        </div>
    );
}


export default LoadingScreen;
