import React from 'react';
import RecipeList from './RecipeList';
import {mount, shallow} from 'enzyme';


jest.mock('axios', () => {

    const recipes = [
        {id: 1, description: 'Taco'},
        {id: 2, description: 'Guacamole'}
    ];
    return {
        get: jest.fn(() => Promise.resolve(recipes))
    }
});

const axios = require('axios');


describe('RecipeList', () => {
    it('render table', () => {
        const wrapper = mount(<RecipeList/>);
        const tableHead = <tr>
            <th>Recipe</th>
            <th>View</th>
            <th>Update</th>
            <th>Delete</th>
        </tr>;
        expect(wrapper.contains(tableHead)).toEqual(true);
    });

    it('work with promise', (done) => {
        const wrapper = shallow(<RecipeList/>);

        wrapper.instance().componentDidMount().then(() => {
            expect(axios.get).toHaveBeenCalled();
            expect(axios.get).toHaveBeenCalledWith("http://localhost:8080/api/");


            done();
        });

    })
});
