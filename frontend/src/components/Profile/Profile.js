// src/components/Profile.jsx
import React, { useState } from 'react';
import ProfileInfo from './ProfileInfo';
import UserPayments from './UserPanel/UserPayments';
import UserLicenses from './UserPanel/UserLicenses';
import LicenseDetailsModal from './UserPanel/LicenseDetailsModal';
import AdminPanel from './AdminPanel/AdminPanel';
import './Profile.css';

const Profile = ({ user, payments, licenses }) => {
  const [selectedLicense, setSelectedLicense] = useState(null);

  const formatDuration = (duration) => {
    if (!duration && duration !== 0) return '--:--';
    const minutes = Math.floor(duration / 60);
    const seconds = duration % 60;
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  };

  if (!user) return <div>Загрузка...</div>;
  const isAdmin = user.role?.includes('ADMIN');

  return (
    <div className="profile-container">
      <ProfileInfo user={user} />
      {!isAdmin && (
        <>
          <UserPayments payments={payments} />
          <UserLicenses licenses={licenses} onSelect={setSelectedLicense} />
        </>
      )}
      {selectedLicense && !isAdmin && (
        <LicenseDetailsModal license={selectedLicense} onClose={() => setSelectedLicense(null)} formatDuration={formatDuration} />
      )}
      {isAdmin && <AdminPanel user={user} formatDuration={formatDuration} />}
    </div>
  );
};

export default Profile;

