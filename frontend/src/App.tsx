import "./App.scss";
import axios from 'axios';
import MainPage from "./routes/MainPage/MainPage";
import AuthMain from "./routes/auth/AuthMain";
import {useEffect, useState} from "react";

function App() {

    const [user, setUser] = useState("This is a user");

    useEffect(() => {
        axios.get(`http://localhost:8080/users/me`)
            .then(res => {
                const user = res.data;
                setUser(user);
            })
    });

    return (
        <>
            {user ?
                <MainPage/>
                :
                <AuthMain/>

            }
        </>
    );
}

export default App;
