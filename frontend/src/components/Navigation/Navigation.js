// src/components/Navigation.jsx
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

const Navigation = () => {
  const navigate = useNavigate();
  const { isAuthenticated, logout, user } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isAdmin = user?.role === 'ADMIN';

  return (
    <nav className="main-nav d-flex justify-content-between align-items-center">
      <div className="nav-left d-flex">
        <Link to="/">Главная</Link>
        <Link to="/tracks">Треки</Link>
        <Link to="/playlists">Плейлисты</Link>
      </div>
      <div className="nav-right d-flex">
        {isAuthenticated ? (
          <>
            <Link to="/profile">Личный кабинет</Link>
            {isAdmin && <Link to="/admin">Панель администратора</Link>}
            <button onClick={handleLogout}>Выйти</button>
          </>
        ) : (
          <>
            <Link to="/login">Войти</Link>
            <Link to="/register">Регистрация</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navigation; 