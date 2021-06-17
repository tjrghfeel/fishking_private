import React from 'react';

class ContentView extends React.Component{
    constructor(props) {
        super(props);

        this.state={
            content:[]
        }
    }

    componentDidMount() {
        if (this.props.content) {
            this.setState({content: this.props.content.split('\n')})
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if(prevProps.content !== this.props.content){
            this.setState({content:this.props.content.split('\n')});
        }
    }

    render() {
        return(
            <>
                {this.state.content.map(item=>{
                    return(
                        <>
                            {item.split(' ').map(item2 =>{
                                return(
                                    <>{item2}&nbsp;</>
                                );
                            })}
                            <br/>
                        </>
                    );
                })}
            </>
        )
    }
}
export default ContentView;