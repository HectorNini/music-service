// src/pages/LoginPage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import Login from '../components/Login/Login';
import { useAuth } from '../contexts/AuthContext';

const LoginPage = () => {
  // Исправление: Добавляем состояние для учетных данных и ошибок
  const [credentials, setCredentials] = useState({
    username: '',
    password: ''
  });
  const [error, setError] = useState('');

  const navigate = useNavigate();
  const { login } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/auth/login', credentials);
      await login(response.data.token);
      navigate('/profile');
    } catch (err) {
      setError('Неверные учетные данные');
    }
  };

  return (
    <Login
      username={credentials.username}
      password={credentials.password}
      error={error}
      onUsernameChange={(e) => setCredentials({ ...credentials, username: e.target.value })}
      onPasswordChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
      onSubmit={handleLogin}
    />
  );
};

export default LoginPage;



