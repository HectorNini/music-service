import React from 'react';

const Login = ({ username, password, error, onUsernameChange, onPasswordChange, onSubmit }) => {
  return (
    <div className="auth-container">
      <h2>Login</h2>
      {error && <div className="error">{error}</div>}
      <form onSubmit={onSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={onUsernameChange}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={onPasswordChange}
          required
        />
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default Login;