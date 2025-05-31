import React from 'react';
import './Profile.css';

const UserLicenses = ({ licenses, onSelect }) => (
  <div className="licenses card-panel mb-30">
    <h3>Лицензии</h3>
    {licenses.length > 0 ? (
      licenses.map(license => (
        <div 
          key={license.licenseId} 
          className="license-item"
          onClick={() => onSelect(license)}
        >
          <p><strong>Продукт:</strong> {license.productName}</p>
          <p><strong>Действует до:</strong> {new Date(license.endDate).toLocaleDateString()}</p>
        </div>
      ))
    ) : (
      <div>Нет активных лицензий</div>
    )}
  </div>
);

export default UserLicenses; 