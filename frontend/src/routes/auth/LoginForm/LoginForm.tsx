import '../auth.scss'
import {Button, Form} from "react-bootstrap";

export default function LoginForm(){
    return(
        <div className="auth_form">
            <Form>
                <Form.Group className="mb-3" controlId="loginForm.email">
                    <Form.Label>Username</Form.Label>
                    <Form.Control type="text" placeholder="Pepenator3000" />
                </Form.Group>
                <Form.Group className="mb-3" controlId="loginForm.password">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" placeholder="********" />
                </Form.Group>
            </Form>
            <Button>Lets Go 🚀</Button>
        </div>
    )
}
