import React from 'react';
import {shallow} from 'enzyme';
import RecipeForm from './RecipeForm';
import {FormErrors} from './FormErrors';

describe('ReciepForm', () => {

    it('should submit form change submitted to true', async () => {
        const wrapper = shallow(<RecipeForm/>);
        const form = wrapper.find('form');
        expect(wrapper.state().submitted).toEqual(false);
        form.simulate('submit', { preventDefault () {}});
        await wrapper.update();
        expect(wrapper.state().submitted).toEqual(true);
    });

    it('should submit call handleSubmit once', () => {
        const mockSubmit = jest.fn();
        const wrapper = shallow(<RecipeForm/>);
        wrapper.instance().handleSubmit = mockSubmit;
        wrapper.update();
        wrapper.instance().handleSubmit();
        expect(mockSubmit).toBeCalled();
    });

    describe('simulate change',  () => {
        it('input', async () => {
            const tree = shallow(<RecipeForm/>);
            expect(tree.state().recipe.description).toBe('');
            tree.find('form').find('#description').simulate('change', { target: { name: 'description', value: 'desc'}});
            tree.update();
            expect(tree.state().recipe.description).toEqual('desc');


        })
    });

    describe('when form invalid', () => {
        it('should show validation error for description', async () => {
            const tree = shallow(<RecipeForm/>);
            tree.find('form').find('#description').simulate('change', { target: { name: 'description', value: 'de'}});
            expect(tree.find(FormErrors).dive().find('p').text()).toEqual('description is invalid');
        });

        it('should show validation error for preparation time', async () => {
            const tree = shallow(<RecipeForm/>);
            tree.find('form').find('#prepTime').simulate('change', { target: { name: 'prepTime', value: 0}});
            expect(tree.find(FormErrors).dive().find('p').text()).toEqual('prepTime is invalid');
        });

        it('should show validation error for cooking time', async () => {
            const tree = shallow(<RecipeForm/>);
            tree.find('form').find('#cookTime').simulate('change', { target: { name: 'cookTime', value: 0}});
            expect(tree.find(FormErrors).dive().find('p').text()).toEqual('cookTime is invalid');
        })


    });

    describe('when update props true', () => {
        const tree = shallow(<RecipeForm update={true} id={1}/>)
    })
});
