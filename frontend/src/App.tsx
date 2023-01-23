import "./App.scss";
import axios from 'axios';
import MainPage from "./routes/MainPage/MainPage";
import AuthMain from "./routes/auth/AuthMain";
import {useEffect, useState} from "react";

function App() {

    const [user, setUser] = useState();
    const [token, setToken] = useState<string>();

    const handleToken = (newToken: string) => {
        console.log(token)
        setToken(newToken);
    };

    useEffect(() => {
        axios.get(`http://localhost:8080/users/me`, {
            headers: {
                "Authorization": token
            }
        })
            .then(res => {
                const user = res.data;
                setUser(user);
            })
    }, []);

    return (
        <>
            {user ?
                <MainPage/>
                :
                <AuthMain handleToken={handleToken}/>

            }
        </>
    );
}

export default App;
