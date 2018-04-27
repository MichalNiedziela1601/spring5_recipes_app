import React from 'react';
import {mount} from 'enzyme';
import RecipeDetails from '../recipe/RecipeDetails';
import axios from 'axios';

describe('RecipeDetails', () => {

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