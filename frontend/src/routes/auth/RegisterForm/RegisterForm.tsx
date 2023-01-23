import '../auth.scss'
import {Button, Form} from "react-bootstrap";
import {useState} from "react";
import axios from "axios";

export default function RegisterForm(handleToken: any) {

    const [username, setUsername] = useState<string>();
    const [password, setPassword] = useState<string>();
    const [passwordConfirm, setPasswordConfirm] = useState<string>();
    const [errorMessage, setErrorMessage] = useState<any>();

    const registerNewAccount = () => {
        password === passwordConfirm
            ? axios.post(`http://localhost:8080/register`, {
                username: username,
                password: password
            }).catch(
                function (error) {
                    if (error.response) {
                        setErrorMessage(error.response.data)
                    }
                })
                .then(res => handleToken(res?.data))
            : setErrorMessage("Passwords don't match")
    }

    return (
        <div className="auth_form">
            <Form>
                <Form.Group className="mb-3" controlId="loginForm.username">
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
                <Form.Group className="mb-3" controlId="loginForm.password-repeat">
                    <Form.Label>Repeat Password</Form.Label>
                    <Form.Control type="password" placeholder="********" onChange={e => {
                        setPasswordConfirm(e.target.value);
                        setErrorMessage(null)
                    }}/>
                </Form.Group>
                {
                    errorMessage &&
                    <p className="login_error">{errorMessage}</p>
                }
            </Form>
            <Button onClick={registerNewAccount}>Lets Go 🚀</Button>
        </div>
    )
}
