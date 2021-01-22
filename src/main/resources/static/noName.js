'use strict';

const e = React.createElement;


class NoName extends React.Component {
    constructor(props) {
        super(props);
        this.state = { liked: false };
    }

    render() {
        if (this.state.liked) {
            return 'You liked this.';
        }

        return (
            <button>like</button>
        );
    }
}

const domContainer = document.querySelector("#noName");
ReactDOM.render(e(NoName),domContainer);