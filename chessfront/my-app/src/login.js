import React from 'react';
import './login.css';

export class Login extends React.Component {
    render() {
        return (
            <div className="page">
                <form>
                    <label>
                        Username:
                        <input type="text" name="username" /><br/>
                        Password:
                        <input type="password" name="password" /><br/>
                    </label>
                    <input type="submit" value="Login"/>
                </form>
            </div>
        )
    }
}