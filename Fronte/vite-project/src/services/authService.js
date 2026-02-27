import api from './api';

export const authService = {
    // Đăng nhập
    login: async (username, password) => {
        const response = await api.post('/auth/login', { username, password });
        const { token, user } = response.data.data;
        // Lưu token và user vào localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
        return response.data;
    },

    // Đăng ký
    register: async (formData) => {
        const response = await api.post('/auth/register', {
            username: formData.username,
            email: formData.email,
            password: formData.password,
            fullName: formData.fullName,
            phoneNumber: formData.phone,
        });
        return response.data;
    },

    // Đăng xuất
    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
    },

    // Lấy user hiện tại từ localStorage
    getCurrentUser: () => {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    },

    // Kiểm tra đã đăng nhập chưa
    isLoggedIn: () => {
        return !!localStorage.getItem('token');
    },
};

export default authService;
