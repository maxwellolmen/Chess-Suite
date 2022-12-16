import React from 'react';
import ReactDOM from 'react-dom/client';
import io from 'socket.io-client';

class Socket extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: io("localhost:3001")
        }
    }

    render() {
        return (
            <h1>hi</h1>
        );
    }
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<Socket />);