import React, {Component} from 'react';

export default class RecipeDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {
            recipe:{},
            id: this.props.match.params.id
        }
    }

    componentDidMount() {

    }

    render() {
        return (
            <div className="container">

            </div>
        )
    }
}