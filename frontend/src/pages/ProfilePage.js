// src/pages/ProfilePage.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import Profile from '../components/Profile';
import { useAuth } from '../contexts/AuthContext';



const ProfilePage = () => {
  const { isAuthenticated, logout } = useAuth();
  const [user, setUser] = useState(null);
  const [payments, setPayments] = useState([]);
  const [licenses, setLicenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    const fetchData = async () => {
      try {
        const [userRes, paymentsRes, licensesRes] = await Promise.all([
          api.get('/user'),
          api.get('/payments/me'),
          api.get('/licenses/active')
        ]);

        console.log('User data:', userRes.data); // Проверьте данные пользователя
        console.log('Payments:', paymentsRes.data); // Проверьте платежи
        console.log('Licenses:', licensesRes.data); // Проверьте лицензии
        
        setUser(userRes.data);
        setPayments(paymentsRes.data);
        setLicenses(licensesRes.data);
      } catch (err) {
        console.error('Error:', err);
        if (err.response?.status === 401) {
            logout(); // Важно: очищаем состояние
            navigate('/login');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [isAuthenticated, navigate]);

  if (loading) return <div>Загрузка...</div>;
  if (!user) return <div>Ошибка загрузки данных пользователя</div>;
    if (!Array.isArray(payments)) return <div>Ошибка загрузки платежей</div>;
    if (!Array.isArray(licenses)) return <div>Ошибка загрузки лицензий</div>;

  return (
    <Profile 
      user={user}
      payments={payments}
      licenses={licenses}
    />
  );
};

export default ProfilePage;