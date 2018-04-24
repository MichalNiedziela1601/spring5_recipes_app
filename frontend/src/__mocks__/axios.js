const BASE_URL = 'http://localhost:8080/api/';
export default {
    get: jest.fn((url) => {
        switch(url) {
            case BASE_URL: {
                return  Promise.resolve({ data: [{id: 1}, {id: 2}] });
                break;
            }
            case BASE_URL + '/recipe/1/show': {
                return Promise.resolve({data: {id: 1, description: 'Taco'}});
            }
        }
    })
};