import axios from 'axios';

const API_URL = 'http://localhost:8080';

const instance = axios.create({
    baseURL: API_URL,
});

instance.interceptors.request.use(config => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default instance;
