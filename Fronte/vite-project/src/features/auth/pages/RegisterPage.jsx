import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './LoginPage.css';
import authService from '../../../services/authService';

const RegisterPage = () => {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        username: '',
        fullName: '',
        email: '',
        phone: '',
        password: '',
        confirmPassword: ''
    });
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
        setError('');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (form.password !== form.confirmPassword) {
            setError('Mật khẩu xác nhận không khớp!');
            return;
        }
        setLoading(true);
        setError('');
        try {
            await authService.register(form);
            setSuccess('Đăng ký thành công! Đang chuyển đến trang đăng nhập...');
            setTimeout(() => navigate('/login'), 1500);
        } catch (err) {
            const msg = err.response?.data?.message || 'Đăng ký thất bại. Vui lòng thử lại.';
            setError(msg);
        } finally {
            setLoading(false);
        }
    };

    const passwordMatch = form.confirmPassword && form.password === form.confirmPassword;
    const passwordNotMatch = form.confirmPassword && form.password !== form.confirmPassword;

    return (
        <div className="login-page">
            <div className="login-container">
                <div className="auth-header">
                    <h2>Đăng Ký</h2>
                    <div className="auth-divider-line"></div>
                    <p>Tạo tài khoản để đặt vé nhanh hơn!</p>
                </div>

                {error && <div className="error-alert">{error}</div>}
                {success && (
                    <div className="error-alert" style={{ background: '#f0fff4', borderColor: '#2ecc71', color: '#2ecc71' }}>
                        {success}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">Tên đăng nhập *</label>
                        <input
                            id="username"
                            type="text"
                            name="username"
                            value={form.username}
                            onChange={handleChange}
                            placeholder="Nhập tên đăng nhập..."
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="fullName">Họ và tên</label>
                        <input
                            id="fullName"
                            type="text"
                            name="fullName"
                            value={form.fullName}
                            onChange={handleChange}
                            placeholder="Nhập họ và tên..."
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email *</label>
                        <input
                            id="email"
                            type="email"
                            name="email"
                            value={form.email}
                            onChange={handleChange}
                            placeholder="Nhập địa chỉ email..."
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="phone">Số điện thoại</label>
                        <input
                            id="phone"
                            type="tel"
                            name="phone"
                            value={form.phone}
                            onChange={handleChange}
                            placeholder="Nhập số điện thoại..."
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Mật khẩu *</label>
                        <div className="password-wrapper">
                            <input
                                id="password"
                                type={showPassword ? 'text' : 'password'}
                                name="password"
                                value={form.password}
                                onChange={handleChange}
                                placeholder="Tối thiểu 6 ký tự..."
                                required
                            />
                            <button
                                type="button"
                                className="toggle-pw"
                                onClick={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? '🙈' : '👁'}
                            </button>
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirmPassword">Xác nhận mật khẩu *</label>
                        <input
                            id="confirmPassword"
                            type="password"
                            name="confirmPassword"
                            value={form.confirmPassword}
                            onChange={handleChange}
                            placeholder="Nhập lại mật khẩu..."
                            required
                            style={{
                                borderColor: passwordMatch ? '#2ecc71'
                                           : passwordNotMatch ? '#e74c3c'
                                           : undefined
                            }}
                        />
                        {passwordNotMatch && (
                            <span style={{ fontSize: 12, color: '#e74c3c', marginTop: 4, display: 'block' }}>
                                Mật khẩu không khớp!
                            </span>
                        )}
                        {passwordMatch && (
                            <span style={{ fontSize: 12, color: '#2ecc71', marginTop: 4, display: 'block' }}>
                                Mật khẩu khớp ✓
                            </span>
                        )}
                    </div>

                    <button type="submit" className="btn-submit" disabled={loading}>
                        {loading ? 'ĐANG TẠO TÀI KHOẢN...' : 'TẠO TÀI KHOẢN'}
                    </button>
                </form>

                <p className="switch-auth">
                    Đã có tài khoản?{' '}
                    <Link to="/login">Đăng nhập ngay</Link>
                </p>
            </div>
        </div>
    );
};

export default RegisterPage;
