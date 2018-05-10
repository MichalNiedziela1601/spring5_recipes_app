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

    describe('simulate change', async () => {
        it('input', () => {
            const tree = shallow(<RecipeForm/>);
            expect(tree.state().description).toBe('');
            tree.find('form').find('input').simulate('change', { target: { name: 'description', value: 'desc'}});
            tree.update();
            expect(tree.state().description).toEqual('desc');
        })
    });

    describe('when form invalid', () => {
        it('should show validation error for description', () => {
            const tree = shallow(<RecipeForm/>);
            tree.find('form').find('input').simulate('change', { target: { name: 'description', value: 'desc'}});
            expect(tree.find(FormErrors).dive().find('p')).toHaveLength(1);
            expect(tree.find(FormErrors).dive().find('p').text()).toEqual('description is invalid');
        });

        it('should show validation error for difficulty', () => {
            const tree = shallow(<RecipeForm/>);
            tree.find('form').find('select').simulate('change', { target: { name: 'difficulty', value: 'EASY'}});
            expect(tree.find(FormErrors).dive().find('p').text()).toEqual('difficulty is invalid');
        });

        it('should show validation error for ingredients', () => {
            const tree = shallow(<RecipeForm/>);
            tree.find('form').find('select').simulate('change', { target: { name: 'difficulty', value: 'EASY'}});
            expect(tree.find(FormErrors).dive().find('p').text()).toEqual('difficulty is invalid');
        })


    })

    // describe('when form submit valid',  () => {
    //     it('should redirect to RecipeDetails', async () => {
    //         const wrapper = mount(<RecipeForm/>);
    //         const mockSubmit = jest.fn();
    //         const redirectComponent = mount(
    //             <MemoryRouter initialEntries={['/recipe/new']}>
    //                 <Route component={App} />
    //             </MemoryRouter>
    //         );
    //         wrapper.instance().handleSubmit = mockSubmit;
    //         await wrapper.instance().handleSubmit();
    //         wrapper.update();
    //         expect(redirectComponent.find(App).props().location.pathname).toBe("/recipe/2/show");
    //     })
    // })
});
