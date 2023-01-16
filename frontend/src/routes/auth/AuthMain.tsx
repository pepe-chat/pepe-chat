import {useState} from "react";
import LoginForm from "./LoginForm/LoginForm";
import RegisterForm from "./RegisterForm/RegisterForm";
import './auth_main.scss';

export default function AuthMain() {
    const [login, setLogin] = useState<boolean>(true);
    const [register, setRegister] = useState<boolean>(false);
    return (
        <div className="auth_container">
            <div className="auth">
                {login &&
                    <>
                        <LoginForm/>
                        Don't have an account yet? <p className="auth_link" onClick={() => {
                        setLogin(false);
                        setRegister(true)
                    }}>Create one!</p>
                    </>
                }
                {register &&
                    <>
                        <RegisterForm/>
                        Already have an account? <p className="auth_link" onClick={() => {
                        setLogin(true);
                        setRegister(false)
                    }}>Log in!</p>
                    </>
                }
            </div>
        </div>
    );
}
