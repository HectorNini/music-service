import React from 'react';


const UserLicenses = ({ licenses, onSelect }) => (
  <div className="licenses">
    <h3 className="section-heading">Лицензии</h3>
    <div className="items-list">
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
  </div>
);

export default UserLicenses; 