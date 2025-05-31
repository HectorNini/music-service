import React from 'react';
import './Profile.css';

const LicenseDetailsModal = ({ license, onClose, formatDuration }) => {
  if (!license) return null;
  return (
    <div className="modal" onClick={onClose}>
      <div className="modal-content" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Детали лицензии</h2>
          <button className="close-button" onClick={onClose}>×</button>
        </div>
        <div className="license-details">
          <p><strong>Продукт:</strong> {license.productName}</p>
          <p><strong>Действует до:</strong> {new Date(license.endDate).toLocaleDateString()}</p>
          {license.tracks && license.tracks.length > 0 && (
            <div className="tracks-list">
              <h3>Треки:</h3>
              <div className="tracks-grid">
                {license.tracks.map(track => (
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
  );
};

export default LicenseDetailsModal; 