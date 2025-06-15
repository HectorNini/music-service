import React from 'react';

const Register = ({ formData, error, onSubmit, onInputChange }) => {
  return (
    <div className="auth-container">
      <h2>Регистрация</h2>
      {error && <div className="error">{error}</div>}
      <form onSubmit={onSubmit}>
        <input
          type="text"
          placeholder="Имя пользователя"
          name="username"
          value={formData.username}
          onChange={(e) => onInputChange(e.target.name, e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Пароль"
          name="password"
          value={formData.password}
          onChange={(e) => onInputChange(e.target.name, e.target.value)}
          required
        />
        <input
          type="email"
          placeholder="Email"
          name="email"
          value={formData.email}
          onChange={(e) => onInputChange(e.target.name, e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Полное имя"
          name="fullName"
          value={formData.fullName}
          onChange={(e) => onInputChange(e.target.name, e.target.value)}
          required
        />
        <button type="submit">Зарегистрироваться</button>
      </form>
    </div>
  );
};

export default Register; 