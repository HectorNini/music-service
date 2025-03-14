import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import Register from '../components/Register';

const RegisterPage = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    fullName: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/auth/register', formData);
      navigate('/login');
    } catch (err) {
      setError(err.response?.data || 'Registration failed');
    }
  };

  const handleInputChange = (name, value) => {
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  return (
    <Register 
      formData={formData}
      error={error}
      onSubmit={handleSubmit}
      onInputChange={handleInputChange}
    />
  );
};

export default RegisterPage;