// src/components/Profile.jsx
import React, { useState } from 'react';
import './Profile.css';

// src/components/Profile.jsx
const Profile = ({ user, payments, licenses }) => {
    const [selectedLicense, setSelectedLicense] = useState(null);
  
    const formatDuration = (duration) => {
      if (!duration && duration !== 0) return '--:--';
      const minutes = Math.floor(duration / 60);
      const seconds = duration % 60;
      return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    };
  
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
              <div key={payment.paymentId} className="payment-item">
                <p><strong>Товар:</strong> {payment.productDescription}</p>
                <p><strong>Сумма:</strong> ${payment.amount}</p>
                <p><strong>Дата:</strong> {new Date(payment.paymentDate).toLocaleDateString()}</p>
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
              <div 
                key={license.licenseId} 
                className="license-item"
                onClick={() => setSelectedLicense(license)}
              >
                <p><strong>Продукт:</strong> {license.productName}</p>
                <p><strong>Действует до:</strong> {new Date(license.endDate).toLocaleDateString()}</p>
              </div>
            ))
          ) : (
            <div>Нет активных лицензий</div>
          )}
        </div>

        {selectedLicense && (
          <div className="modal" onClick={() => setSelectedLicense(null)}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
              <div className="modal-header">
                <h2>Детали лицензии</h2>
                <button className="close-button" onClick={() => setSelectedLicense(null)}>×</button>
              </div>
              <div className="license-details">
                <p><strong>Продукт:</strong> {selectedLicense.productName}</p>
                <p><strong>Действует до:</strong> {new Date(selectedLicense.endDate).toLocaleDateString()}</p>
                
                {selectedLicense.tracks && selectedLicense.tracks.length > 0 && (
                  <div className="tracks-list">
                    <h3>Треки в плейлисте:</h3>
                    <div className="tracks-grid">
                      {selectedLicense.tracks.map(track => (
                        <div key={track.trackId} className="track-item">
                          <div className="track-info">
                            <h4>{track.title || 'Без названия'}</h4>
                            <p>{track.artist || 'Неизвестный исполнитель'}</p>
                            <p className="duration">{formatDuration(track.duration)}</p>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    );
  };

export default Profile;

