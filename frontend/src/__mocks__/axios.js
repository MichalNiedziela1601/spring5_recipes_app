const BASE_URL = 'http://localhost:8080/api/';
export default {
    get: jest.fn((url) => {
        switch(url) {
            case BASE_URL: {
                return  Promise.resolve({ data: [{id: 1}, {id: 2}] });
            }
            case BASE_URL + 'recipe/show/1': {
                return Promise.resolve({data: {id: 1, description: 'Taco'}});
            }
        }
    }),
    post: jest.fn((url, obj) => {
        switch(url) {
            case `${BASE_URL}recipe/new` : {
                return Promise.resolve({data: {id: 2}});
            }
        }

    })
};