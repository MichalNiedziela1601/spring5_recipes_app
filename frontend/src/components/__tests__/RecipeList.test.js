import React from 'react';
import RecipeList from '../recipeList/RecipeList';
import RecipeListItem from '../recipeListItem/RecipeListItem';
import {mount, shallow} from 'enzyme';
import axios from 'axios';

describe('RecipeList', () => {

    it('render table', () => {
        const wrapper = shallow(<RecipeList/>);
        const tableHead = <tr>
            <th>Recipe</th>
            <th>Update</th>
            <th>Delete</th>
        </tr>;
        expect(wrapper.contains(tableHead)).toEqual(true);
        wrapper.setState({ recipes: [{id: 1}, {id: 2}]});
        expect(wrapper.find(RecipeListItem)).toHaveLength(2);
    });

    it('work with promise', async (done) => {
        const wrapper = mount(<RecipeList/>);
        expect(wrapper.state('recipes')).toEqual([]);

        wrapper.instance().componentDidMount().then(() => {
            expect(axios.get).toHaveBeenCalled();
            expect(axios.get).toHaveBeenCalledWith("http://localhost:8080/api/");
            expect(wrapper.state().recipes).toEqual([{id: 1}, {id: 2}]);
            done();
        });

    })

});
