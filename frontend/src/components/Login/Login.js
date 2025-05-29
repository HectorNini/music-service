import React from 'react';
import './Login.css';

const Login = ({ username, password, error, onUsernameChange, onPasswordChange, onSubmit }) => {
  return (
    <div className="auth-container">
      <h2>Вход в систему</h2>
      {error && <div className="error">{error}</div>}
      <form onSubmit={onSubmit}>
        <div className="form-group">
          <input
            type="text"
            placeholder="Имя пользователя"
            value={username}
            onChange={onUsernameChange}
            required
          />
        </div>
        <div className="form-group">
          <input
            type="password"
            placeholder="Пароль"
            value={password}
            onChange={onPasswordChange}
            required
          />
        </div>
        <button type="submit" className="submit-button">Войти</button>
      </form>
    </div>
  );
};

export default Login; 