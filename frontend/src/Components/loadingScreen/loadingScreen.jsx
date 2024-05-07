import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './LoadingScreen.css';

function LoadingScreen() {
    const location = useLocation();
    const navigate = useNavigate();
    const username = location.state?.username;

    useEffect(() => {
        const timer = setTimeout(() => {
            navigate("/game", { state: { username: username } });
        }, 5000);

        return () => clearTimeout(timer);
    }, [navigate, username]);

    return (
        <div className="among-us-loading-screen">
            <div className="loading-text"></div>
            <div className="runner"></div>
        </div>
    );
}

export default LoadingScreen;
