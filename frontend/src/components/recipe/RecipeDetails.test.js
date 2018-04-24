import React from 'react';
import {mount, shallow} from 'enzyme';
import axios from 'axios';
import RecipeDetails from './RecipeDetails';

describe('RecipeDetails', () => {

    it('should have set id property', () => {
        const wrapper = shallow(<RecipeDetails match={{params: {id: 1}}}/>);
        expect(wrapper.containsMatchingElement(<h2>Id: 1</h2>)).toBeTruthy();

    });

    it('componentDidMount should fetch recipe detail', async (done) => {
        const wrapper = mount(<RecipeDetails match={{params: {id: 1}}}/>);
        expect(wrapper.state('recipe')).toEqual({});

        wrapper.instance().componentDidMount().then(() => {
            expect(axios.get).toHaveBeenCalled();
            expect(axios.get).toHaveBeenCalledWith("http://localhost:8080/api/recipe/show/1");
            expect(wrapper.state('recipe')).toEqual({id: 1, description: 'Taco'});

            done();
        })
    })
})