// src/components/Profile.jsx
import React, { useState } from 'react';

// src/components/Profile.jsx
const Profile = ({ user, payments, licenses }) => {
    // Добавим защиту от undefined
    if (!user) return <div>Загрузка...</div>;
  
    return (
      <div className="profile-container">
        {/* Профиль */}
        <div className="profile-info">
          <h3>Профиль</h3>
          <p><strong>Логин:</strong> {user.username}</p>
          <p><strong>Email:</strong> {user.email}</p>
          <p><strong>Имя:</strong> {user.fullName}</p>
        </div>
  
        {/* Платежи */}
        <div className="payments">
          <h3>Платежи</h3>
          {payments.length > 0 ? (
            payments.map(payment => (
              <div key={payment.paymentId}>
                <p>Товар: {payment.productDescription}</p>
                <p>Сумма: ${payment.amount}</p>
                <p>Дата: {new Date(payment.paymentDate).toLocaleDateString()}</p>
              </div>
            ))
          ) : (
            <div>Нет платежей</div>
          )}
        </div>
  
        {/* Лицензии */}
        <div className="licenses">
          <h3>Лицензии</h3>
          {licenses.length > 0 ? (
            licenses.map(license => (
              <div key={license.licenseId}>
                <p>Продукт: {license.productName}</p>
                <p>Действует до: {new Date(license.endDate).toLocaleDateString()}</p>
              </div>
            ))
          ) : (
            <div>Нет активных лицензий</div>
          )}
        </div>
      </div>
    );
  };

export default Profile;

