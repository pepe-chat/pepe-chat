import "./main_page.scss";
import SettingsIcon from '@mui/icons-material/Settings';
import PersonIcon from '@mui/icons-material/Person';
import {useState} from "react";
import {Modal} from "react-bootstrap";

function MainPage() {
    const [userView, setUserView] = useState(false);

    return (
        <div className="app-container">
            <div className="header">PEPE CHAT 🐸</div>
            <div className="servers"></div>
            <div className="profile">
                <p className="icon-container">
                    <SettingsIcon/>
                </p>
                <p className="icon-container" onClick={event => {
                    setUserView(true)
                }}>
                    <PersonIcon/>
                </p>
            </div>
            <div className="person-modal">
                {userView &&
                    <Modal>
                        This Is a Modal
                    </Modal>
                }
            </div>
            <div className="rooms"></div>
            <div className="chat"></div>
        </div>
    );
}

export default MainPage;
