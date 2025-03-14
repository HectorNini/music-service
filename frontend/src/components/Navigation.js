// src/components/Navigation.jsx
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Navigation = () => {
  const navigate = useNavigate();
  const { isAuthenticated, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="main-nav">
      <div className="nav-left">
        <Link to="/">Главная</Link>
        <Link to="/tracks">Треки</Link>
        <Link to="/playlists">Плейлисты</Link>
      </div>
      <div className="nav-right">
        {isAuthenticated ? (
          <>
            <Link to="/profile">Личный кабинет</Link>
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