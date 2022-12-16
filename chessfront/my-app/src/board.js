import React from 'react';
import './board.css';

function Square(props) {
    return (
        <button className="square" onClick={props.onClick} color={props.color}>
            {props.value}
        </button>
    );
}

class Board extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            squares: Array(64).fill(null),
        };
    }

    handleClick(f, r) {

    }

    renderSquare(f, r) {
        return (
            <Square
                value={this.state.squares[r * 7 + f]}
                onClick={() => this.handleClick(f, r)}
                color={(r * 7 + f) % 2 === 0 ? '#fff' : '#000'}
            />
        );
    }

    render() {
        return (
            <div>
                <div className="board-row">
                    {this.renderSquare(0, 0)}
                    {this.renderSquare(1, 0)}
                    {this.renderSquare(2, 0)}
                    {this.renderSquare(3, 0)}
                    {this.renderSquare(4, 0)}
                    {this.renderSquare(5, 0)}
                    {this.renderSquare(6, 0)}
                    {this.renderSquare(7, 0)}
                </div>
                <div className="board-row">
                    {this.renderSquare(0, 1)}
                    {this.renderSquare(1, 1)}
                    {this.renderSquare(2, 1)}
                    {this.renderSquare(3, 1)}
                    {this.renderSquare(4, 1)}
                    {this.renderSquare(5, 1)}
                    {this.renderSquare(6, 1)}
                    {this.renderSquare(7, 1)}
                </div>
                <div className="board-row">
                    {this.renderSquare(0, 2)}
                    {this.renderSquare(1, 2)}
                    {this.renderSquare(2, 2)}
                    {this.renderSquare(3, 2)}
                    {this.renderSquare(4, 2)}
                    {this.renderSquare(5, 2)}
                    {this.renderSquare(6, 2)}
                    {this.renderSquare(7, 2)}
                </div>
                <div className="board-row">
                    {this.renderSquare(0, 3)}
                    {this.renderSquare(1, 3)}
                    {this.renderSquare(2, 3)}
                    {this.renderSquare(3, 3)}
                    {this.renderSquare(4, 3)}
                    {this.renderSquare(5, 3)}
                    {this.renderSquare(6, 3)}
                    {this.renderSquare(7, 3)}
                </div>
                <div className="board-row">
                    {this.renderSquare(0, 4)}
                    {this.renderSquare(1, 4)}
                    {this.renderSquare(2, 4)}
                    {this.renderSquare(3, 4)}
                    {this.renderSquare(4, 4)}
                    {this.renderSquare(5, 4)}
                    {this.renderSquare(6, 4)}
                    {this.renderSquare(7, 4)}
                </div>
                <div className="board-row">
                    {this.renderSquare(0, 5)}
                    {this.renderSquare(1, 5)}
                    {this.renderSquare(2, 5)}
                    {this.renderSquare(3, 5)}
                    {this.renderSquare(4, 5)}
                    {this.renderSquare(5, 5)}
                    {this.renderSquare(6, 5)}
                    {this.renderSquare(7, 5)}
                </div>
                <div className="board-row">
                    {this.renderSquare(0, 6)}
                    {this.renderSquare(1, 6)}
                    {this.renderSquare(2, 6)}
                    {this.renderSquare(3, 6)}
                    {this.renderSquare(4, 6)}
                    {this.renderSquare(5, 6)}
                    {this.renderSquare(6, 6)}
                    {this.renderSquare(7, 6)}
                </div>
                <div className="board-row">
                    {this.renderSquare(0, 7)}
                    {this.renderSquare(1, 7)}
                    {this.renderSquare(2, 7)}
                    {this.renderSquare(3, 7)}
                    {this.renderSquare(4, 7)}
                    {this.renderSquare(5, 7)}
                    {this.renderSquare(6, 7)}
                    {this.renderSquare(7, 7)}
                </div>
            </div>
        );
    }
}

export class Game extends React.Component {
    render() {
        return (
            <div className="page">
                <div className="game-board">
                    <Board />
                </div>
            </div>
        );
    }
}