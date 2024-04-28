import {useNavigate} from 'react-router-dom';
import './LoadingScreen.css';


function LoadingScreen() {
    const navigate = useNavigate();

    setTimeout(() => {
        navigate("/game");
    }, 5000);


    return (
        <div className="among-us-loading-screen">
            <div className="loading-text"></div>
            <div className="runner"></div>
        </div>
    );
}


export default LoadingScreen;
