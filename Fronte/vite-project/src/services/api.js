// import axios from 'axios';

// // Create an Axios instance with default configuration
// const api = axios.create({
//     baseURL: 'http://localhost:8080/api', // Adjust according to your Spring Boot port
//     timeout: 10000,
//     headers: {
//         'Content-Type': 'application/json',
//     },
// });

// // Add a request interceptor (e.g., for adding token)
// api.interceptors.request.use(
//     (config) => {
//         // const token = localStorage.getItem('token');
//         // if (token) {
//         //   config.headers.Authorization = `Bearer ${token}`;
//         // }
//         return config;
//     },
//     (error) => {
//         return Promise.reject(error);
//     }
// );

// // Add a response interceptor (e.g., for handling global errors)
// api.interceptors.response.use(
//     (response) => {
//         return response;
//     },
//     (error) => {
//         // Handle global errors (e.g., 401 Unauthorized)
//         return Promise.reject(error);
//     }
// );

// export default api;

// src/services/api.js
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Tự động gắn token vào mỗi request
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Xử lý 401 - token hết hạn → tự động logout
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login'; // redirect về login
        }
        return Promise.reject(error);
    }
);

export default api;
