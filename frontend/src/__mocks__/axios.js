const BASE_URL = 'http://localhost:8080/api/';
export default {
    get: jest.fn((url) => {
        switch (url) {
            case BASE_URL: {
                return Promise.resolve({data: [{id: 1}, {id: 2}]});
            }
            case BASE_URL + 'recipe/show/1': {
                return Promise.resolve({data: {id: 1, description: 'Taco'}});
            }
            case `${BASE_URL}uoms` : {
                return Promise.resolve({data: [{id: 1, description: 'Each'}]});
            }
            case  `${BASE_URL}category` : {
                return Promise.resolve({data: [{id: 1, description: 'Mexican'}]});
            }
            case BASE_URL + 'recipe/update/1': {
                return Promise.resolve({
                    data: {
                        "id": 1,
                        "description": "Taco",
                        "prepTime": 20,
                        "cookTime": 90,
                        "servings": 4,
                        "source": null,
                        "url": null,
                        "directions": "Prepare chicken breast on the fry pan. Dice paprika, onions and lettuce. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                        "difficulty": "EASY",
                        "ingredients" : {
                            id: 1,
                            amount: 2,
                            description: 'ingredient',
                            uom: {
                                id: 1,
                                description: 'Each'
                            }
                        },
                        notes: {
                            recipeNotes: 'skjfdkgjdfgjdfg'
                        },
                        categories: [
                            {id: 1, description: 'Mexican'}
                        ]
                    }
                });
            }
        }
    }),
    post: jest.fn((url, obj) => {
        switch (url) {
            case `${BASE_URL}recipe/new` : {
                return Promise.resolve({data: {id: 2}});
            }
        }

    })
};