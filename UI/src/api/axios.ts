import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8081',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor to add the token
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token && token.trim() !== '') {
            config.headers.Authorization = `Bearer ${token}`;
        }
        console.log(`🚀 Request to: ${config.baseURL}${config.url}`, {
            headers: config.headers
        });
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Interceptor for handling 401/403 errors
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            // Redirect to login or refresh token
            console.error('Session expired');
            // localStorage.removeItem('token');
            // window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;
