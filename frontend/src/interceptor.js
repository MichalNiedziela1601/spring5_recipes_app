import axios from 'axios';

export default {
    requestIntercept: () => {
        axios.interceptors.request.use((config) => {
            const token = localStorage.getItem('access_token');
            if (token) {
                config.headers['Authorization'] = 'Bearer '.concat(token);
                return config;
            }
        }, (error) => Promise.reject(error));

    },
    responseIntercept: () => {
        axios.interceptors.response.use((response) => response,
            (error) => {
                const originalRequest = error.config;

                if (error.response.status === 401 && error.response.data['error'] === 'invalid_token') {
                    const refreshToken = localStorage.getItem('refresh_token');
                    if(refreshToken) {
                        const data = new FormData();
                        data.append('refresh_token', refreshToken);
                        data.append('grant_type', 'refresh_token');
                        return axios.post('http://localhost:8080/api/oauth/token', data, {
                            headers: {
                                'Content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
                            },
                            auth: {
                                username: 'client',
                                password: 'secret'
                            }
                        })
                            .then(({data}) => {
                            localStorage.setItem('access_token', data['access_token']);
                            localStorage.setItem('refresh_token', data['refresh_token']);
                            axios.defaults.headers.common['Authorization'] = 'Bearer ' + data['access_token'];
                            originalRequest.headers['Authorization'] = 'Bearer ' + data['access_token'];
                            return axios(originalRequest);
                            } )
                            .catch((error) => {
                            console.log(error.response.data);
                            });

                    }
                }

                return Promise.reject(error);
            });
    }
}


