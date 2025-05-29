import React from 'react';

const AdminUsers = ({ users }) => (
  <div className="admin-section">
    <h3>Управление пользователями</h3>
    <div className="admin-table">
      <table>
        <thead>
          <tr>
            <th>Логин</th>
            <th>Email</th>
            <th>Имя</th>
            <th>Роль</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.username}>
              <td>{user.username}</td>
              <td>{user.email}</td>
              <td>{user.fullName}</td>
              <td>{user.role}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
);

export default AdminUsers; 