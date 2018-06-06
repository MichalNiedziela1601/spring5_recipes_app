import React from 'react';
import {mount, shallow} from 'enzyme';
import RecipeDetails from '../recipe/RecipeDetails';
import axios from 'axios';

describe('RecipeDetails', () => {

    it('should render recipe details', () => {
        const wrapper = shallow(<RecipeDetails match={{params: {id: 1}}}/>);
        const recipe = {
            "id": 1,
            "description": "Perfect guacamole",
            "prepTime": 10,
            "cookTime": 10,
            "servings": 2,
            "source": null,
            "url": null,
            "directions": "Guacamole directions. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            "difficulty": "EASY",
            "ingredients": [
                {
                    "id": 1,
                    "description": "ripe avokados",
                    "amount": 2,
                    "uom": {
                        "id": 6,
                        "description": "Each"
                    }
                }
            ],
            "notes": {
                "id": 1,
                "recipeNotes": "Guac Notes. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            },
            "categories": [
                {
                    "id": 1,
                    "description": "American"
                },
                {
                    "id": 3,
                    "description": "Mexican"
                }
            ]
        };
        wrapper.setState({recipe: recipe});
        expect(wrapper.find('h2').text()).toEqual(recipe.description);
        expect(wrapper.find('h6').at(0).text()).toEqual("Directions");
        expect(wrapper.find('.info').find('span').at(1).text()).toEqual("American, Mexican");
        expect(wrapper.find('.info').find('span').at(3).text()).toEqual("EASY");
        expect(wrapper.find('.info').find('span').at(5).text()).toEqual("2");
        expect(wrapper.find('.info').find('span').at(7).text()).toEqual(recipe.prepTime + " mins");
        expect(wrapper.find('.info').find('span').at(9).text()).toEqual("10 mins");
        expect(wrapper.find('ul').at(0).find('li').at(0).text()).toEqual("2 Each ripe avokados");
        expect(wrapper.find('#recipe-notes').text()).toEqual('Guac Notes. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.');
        expect(wrapper.find('.recipe-ingredients').find('ul').at(0).text()).toEqual('2 Each ripe avokados');
        expect(wrapper.find('img').prop('src')).toEqual('http://localhost:8080/api/recipe/1/recipeimage');
        expect(wrapper).toMatchSnapshot();
    });

    it('componentDidMount should fetch recipe detail', async (done) => {
        const wrapper = mount(<RecipeDetails match={{params: {id: 1}}}/>);
        expect(wrapper.state('recipe')).toEqual({});
        expect(wrapper.state('id')).toEqual(1);

        wrapper.instance().componentDidMount().then(() => {
            expect(axios.get).toHaveBeenCalled();
            expect(axios.get).toHaveBeenCalledWith("http://localhost:8080/api/recipe/show/1");
            expect(wrapper.state('recipe')).toEqual({id: 1, description: 'Taco'});
            done();
        })
    })
});