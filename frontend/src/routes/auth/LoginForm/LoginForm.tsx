import '../auth.scss'
import {Button, Form} from "react-bootstrap";
import axios from "axios";
import {useState} from "react";

export default function LoginForm(handleToken: any) {

    const [username, setUsername] = useState<string>();
    const [password, setPassword] = useState<string>();
    const [errorMessage, setErrorMessage] = useState<any>();

    const loginIntoAccount = () => {
        axios.post(`http://localhost:8080/login`, {
            username: username,
            password: password
        }).catch(
            function (error) {
                setErrorMessage(error.response.data)
            })
            .then(res => handleToken(res?.data))
    }

    return (
        <div className="auth_form">
            <Form>
                <Form.Group className="mb-3" controlId="loginForm.email">
                    <Form.Label>Username</Form.Label>
                    <Form.Control type="text" placeholder="Pepenator3000" onChange={e => {
                        setUsername(e.target.value);
                        setErrorMessage(null)
                    }}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="loginForm.password">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" placeholder="********" onChange={e => {
                        setPassword(e.target.value);
                        setErrorMessage(null)
                    }}/>
                </Form.Group>
                {
                    errorMessage &&
                    <p className="login_error">{errorMessage}</p>
                }
            </Form>
            <Button onClick={loginIntoAccount}>Lets Go 🚀</Button>
        </div>
    )
}
