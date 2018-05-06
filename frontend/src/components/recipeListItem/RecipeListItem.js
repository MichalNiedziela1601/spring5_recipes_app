import React, {Component} from 'react';
import './RecipeListItem.css';
import {Link} from 'react-router-dom';

export default class RecipeListItem extends Component {

    notesSlice(notes) {
        let sliceNotes;

        if (notes.length > 125) {
            sliceNotes = notes.slice(0, 125) + '...';
        } else {
            sliceNotes = notes + '...';
        }
        return sliceNotes;
    }

    convertTime(time) {
        let hours = 0;
        let minutes = time;
        if (time >= 60) {
            hours = Math.floor(time / 60);
            minutes = time - hours * 60;
        }
        return [hours, minutes];
    }


    render() {
        const img_url = "http://localhost:8080/api/recipe/" + this.props.recipe.id + "/recipeimage";
        const [hours, minutes] = this.convertTime(this.props.recipe.cookTime);
        let cookingTme;

        switch (hours) {
            case 0 :
                cookingTme = `${minutes} mins`;
                break;
            case 1:
                cookingTme = `${hours} hour ${minutes} mins`;
                break;
            default:
                cookingTme = `${hours} hours ${minutes} mins`;
                break;
        }
        return (
            <div className="row recipe-list-item">
                <div className="col-md-4">
                    <div className="img-wrapper"><img src={img_url} className="img-responsive"/></div>
                </div>
                <div className="col-md-8">
                    <div className="item-header"><h4><Link
                        to={`/recipe/${this.props.recipe.id}/show`}>{this.props.recipe.description}</Link></h4></div>
                    <hr/>
                    <div className="item-bar row">
                        <div className="col-md-7">
                          <span className="text-uppercase">
                              <i className="fa fa-clock-o" aria-hidden="true"></i>
                              <span className="font-weight-bold">Cooking time:
                              </span> {cookingTme}
                              </span></div>
                        <div className="col-md-5">
                            <span className="text-uppercase">
                                <i className="fa fa-user" aria-hidden="true"></i><span
                                className="font-weight-bold">Servings:</span> {this.props.recipe.servings}</span></div>
                    </div>
                    {this.notesSlice(this.props.recipe.notes.recipeNotes)} <Link to={`/recipe/${this.props.recipe.id}/show`}><span
                    className="text-danger">Read More</span></Link>
                </div>

            </div>
        );
    }


}