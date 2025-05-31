import React from 'react';

const AdminLicenses = ({ licenses }) => (
  <div className="admin-section mb-30">
    <h3>Управление лицензиями</h3>
    <div className="admin-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Продукт</th>
            <th>Срок действия</th>
            <th>Треки</th>
            <th>Статус</th>
          </tr>
        </thead>
        <tbody>
          {licenses.map(license => (
            <tr key={license.licenseId}>
              <td>{license.licenseId}</td>
              <td>{license.productName}</td>
              <td>{new Date(license.endDate).toLocaleDateString()}</td>
              <td>{license.tracks?.length || 0}</td>
              <td>
                <span className={new Date(license.endDate) > new Date() ? 'status-active' : 'status-expired'}>
                  {new Date(license.endDate) > new Date() ? 'Активна' : 'Истекла'}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
);

export default AdminLicenses; 