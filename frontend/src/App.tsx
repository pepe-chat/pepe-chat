import "./App.scss";
import SettingsIcon from '@mui/icons-material/Settings';
import PersonIcon from '@mui/icons-material/Person';

function App() {
    return (
        <div className="app-container">
            <div className="header">PEPE CHAT</div>
            <div className="servers"></div>
            <div className="profile">
                <p className="icon-container">
                    <SettingsIcon/>
                </p>
                <p className="icon-container">
                    <PersonIcon/>
                </p>
            </div>
            <div className="rooms"></div>
            <div className="chat"></div>
        </div>
    );
}

export default App;
