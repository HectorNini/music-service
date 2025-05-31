import React from 'react';

const ProfileInfo = ({ user }) => {
  if (!user) return null;
  const isAdmin = user.role?.includes('ADMIN');
  return (
    <div className="profile-info">
      <h3>Профиль</h3>
      <p><strong>Логин:</strong> {user.username}</p>
      <p><strong>Email:</strong> {user.email}</p>
      <p><strong>Имя:</strong> {user.fullName}</p>
      {isAdmin && <p><strong>Роль:</strong> Администратор</p>}
    </div>
  );
};

export default ProfileInfo; 