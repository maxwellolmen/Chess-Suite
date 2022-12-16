import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {Game} from './board.js'
import {Login} from './login.js'

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<Login />);
