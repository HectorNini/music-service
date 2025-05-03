// src/components/Profile.jsx
import React, { useState, useEffect } from 'react';
import api from '../api';
import './Profile.css';

// src/components/Profile.jsx
const Profile = ({ user, payments, licenses }) => {
    const [selectedLicense, setSelectedLicense] = useState(null);
    const [activeTab, setActiveTab] = useState('users'); // 'users', 'tracks', 'playlists', 'licenses'
    const [showCreatePlaylistModal, setShowCreatePlaylistModal] = useState(false);
    const [newPlaylist, setNewPlaylist] = useState({
      name: '',
      description: '',
      trackIds: [],
      price: ''
    });
    const [adminData, setAdminData] = useState({
      users: [],
      tracks: [],
      playlists: [],
      licenses: [],
      payments: []
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
  
    const formatDuration = (duration) => {
      if (!duration && duration !== 0) return '--:--';
      const minutes = Math.floor(duration / 60);
      const seconds = duration % 60;
      return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    };
  
    const fetchAdminData = async (endpoint) => {
      try {
        setLoading(true);
        const response = await api.get(`/${endpoint}`);
        return response.data;
      } catch (err) {
        console.error(`Error fetching ${endpoint}:`, err);
        setError(`Ошибка при загрузке ${endpoint}: ${err.response?.data || err.message}`);
        return [];
      } finally {
        setLoading(false);
      }
    };
  
    useEffect(() => {
      const loadAdminData = async () => {
        if (user?.role?.includes('ADMIN')) {
          try {
            const [tracks, playlists, payments, users, licenses] = await Promise.all([
              fetchAdminData('tracks'),
              fetchAdminData('playlists'),
              fetchAdminData('payments'),
              fetchAdminData('user/all'),
              fetchAdminData('licenses')
            ]);
  
            setAdminData(prev => ({
              ...prev,
              tracks,
              playlists,
              payments,
              users,
              licenses
            }));
          } catch (err) {
            console.error('Error loading admin data:', err);
            setError('Ошибка при загрузке данных администратора');
          }
        }
      };
  
      loadAdminData();
    }, [user]);
  
    const handleAction = async (action, data) => {
      try {
        setLoading(true);
        switch (action) {
          case 'createLicense':
            await api.post('/licenses', data);
            break;
          case 'buyLicense':
            await api.post('/licenses/buy', null, { 
              params: { priceId: data.priceId } 
            });
            break;
          default:
            console.warn('Неизвестное действие:', action);
        }
        // Обновляем данные после действия
        const updatedData = await fetchAdminData('licenses');
        setAdminData(prev => ({ ...prev, licenses: updatedData }));
      } catch (err) {
        console.error('Error performing action:', err);
        setError(`Ошибка при выполнении действия: ${err.response?.data || err.message}`);
      } finally {
        setLoading(false);
      }
    };
  
    const handleCreatePlaylist = async () => {
      try {
        setLoading(true);
        const response = await api.post('/playlists', {
          ...newPlaylist,
          price: newPlaylist.price ? parseFloat(newPlaylist.price) : null
        });
        setAdminData(prev => ({
          ...prev,
          playlists: [...prev.playlists, response.data]
        }));
        setShowCreatePlaylistModal(false);
        setNewPlaylist({
          name: '',
          description: '',
          trackIds: [],
          price: ''
        });
      } catch (err) {
        console.error('Error creating playlist:', err);
        setError(`Ошибка при создании плейлиста: ${err.response?.data || err.message}`);
      } finally {
        setLoading(false);
      }
    };
  
    const handleTrackSelect = (trackId) => {
      setNewPlaylist(prev => ({
        ...prev,
        trackIds: prev.trackIds.includes(trackId)
          ? prev.trackIds.filter(id => id !== trackId)
          : [...prev.trackIds, trackId]
      }));
    };
  
    const calculateTotalPrice = () => {
      return adminData.tracks
        .filter(track => newPlaylist.trackIds.includes(track.trackId))
        .reduce((sum, track) => sum + (track.price || 0), 0);
    };
  
    const handleDeletePlaylist = async (playlistId) => {
      if (window.confirm('Вы уверены, что хотите удалить этот плейлист?')) {
        try {
          setLoading(true);
          await api.delete(`/playlists/${playlistId}`);
          setAdminData(prev => ({
            ...prev,
            playlists: prev.playlists.filter(p => p.playlistId !== playlistId)
          }));
        } catch (err) {
          console.error('Error deleting playlist:', err);
          setError(`Ошибка при удалении плейлиста: ${err.response?.data || err.message}`);
        } finally {
          setLoading(false);
        }
      }
    };
  
    // Добавим защиту от undefined
    if (!user) return <div>Загрузка...</div>;
  
    const isAdmin = user.role?.includes('ADMIN');
  
    const renderAdminPanel = () => {
      if (loading) return <div className="loading">Загрузка...</div>;
      if (error) return <div className="error-message">{error}</div>;
  
      return (
        <div className="admin-panel">
          <div className="admin-tabs">
            <button 
              className={`tab-button ${activeTab === 'users' ? 'active' : ''}`}
              onClick={() => setActiveTab('users')}
            >
              Пользователи
            </button>
            <button 
              className={`tab-button ${activeTab === 'tracks' ? 'active' : ''}`}
              onClick={() => setActiveTab('tracks')}
            >
              Треки
            </button>
            <button 
              className={`tab-button ${activeTab === 'playlists' ? 'active' : ''}`}
              onClick={() => setActiveTab('playlists')}
            >
              Плейлисты
            </button>
            <button 
              className={`tab-button ${activeTab === 'licenses' ? 'active' : ''}`}
              onClick={() => setActiveTab('licenses')}
            >
              Лицензии
            </button>
            <button 
              className={`tab-button ${activeTab === 'payments' ? 'active' : ''}`}
              onClick={() => setActiveTab('payments')}
            >
              Платежи
            </button>
          </div>
  
          <div className="admin-content">
            {activeTab === 'users' && (
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
                      {adminData.users.map(user => (
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
            )}
  
            {activeTab === 'tracks' && (
              <div className="admin-section">
                <h3>Управление треками</h3>
                <div className="admin-table">
                  <table>
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Исполнитель</th>
                        <th>Длительность</th>
                        <th>Цена</th>
                      </tr>
                    </thead>
                    <tbody>
                      {adminData.tracks.map(track => (
                        <tr key={track.trackId}>
                          <td>{track.trackId}</td>
                          <td>{track.title}</td>
                          <td>{track.artist}</td>
                          <td>{formatDuration(track.duration)}</td>
                          <td>${track.price}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}
  
            {activeTab === 'playlists' && (
              <div className="admin-section">
                <h3>Управление плейлистами</h3>
                <div className="admin-actions">
                  <button 
                    className="action-button"
                    onClick={() => setShowCreatePlaylistModal(true)}
                  >
                    Создать плейлист
                  </button>
                </div>
                <div className="admin-table">
                  <table>
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Описание</th>
                        <th>Количество треков</th>
                        <th>Цена</th>
                        <th>Действия</th>
                      </tr>
                    </thead>
                    <tbody>
                      {adminData.playlists.map(playlist => (
                        <tr key={playlist.playlistId}>
                          <td>{playlist.playlistId}</td>
                          <td>{playlist.name}</td>
                          <td>{playlist.description}</td>
                          <td>{playlist.tracks?.length || 0}</td>
                          <td>${playlist.price}</td>
                          <td>
                            <button
                              className="action-button delete-button"
                              onClick={() => handleDeletePlaylist(playlist.playlistId)}
                            >
                              Удалить
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}
  
            {activeTab === 'licenses' && (
              <div className="admin-section">
                <h3>Управление лицензиями</h3>
                <div className="admin-actions">
                  <button 
                    className="action-button"
                    onClick={() => handleAction('createLicense', {})}
                  >
                    Выдать лицензию
                  </button>
                </div>
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
                      {adminData.licenses.map(license => (
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
            )}
  
            {activeTab === 'payments' && (
              <div className="admin-section">
                <h3>Управление платежами</h3>
                <div className="admin-table">
                  <table>
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>Товар</th>
                        <th>Сумма</th>
                        <th>Дата</th>
                        <th>Статус</th>
                        <th>Метод оплаты</th>
                      </tr>
                    </thead>
                    <tbody>
                      {adminData.payments.map(payment => (
                        <tr key={payment.paymentId}>
                          <td>{payment.paymentId}</td>
                          <td>{payment.productDescription}</td>
                          <td>${payment.amount}</td>
                          <td>{new Date(payment.paymentDate).toLocaleDateString()}</td>
                          <td>
                            <span className={`status-${payment.status.toLowerCase()}`}>
                              {payment.status}
                            </span>
                          </td>
                          <td>
                            <span className="payment-method">
                              {payment.paymentMethod}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}
          </div>
        </div>
      );
    };
  
    return (
      <div className="profile-container">
        {/* Профиль */}
        <div className="profile-info">
          <h3>Профиль</h3>
          <p><strong>Логин:</strong> {user.username}</p>
          <p><strong>Email:</strong> {user.email}</p>
          <p><strong>Имя:</strong> {user.fullName}</p>
          {isAdmin && <p><strong>Роль:</strong> Администратор</p>}
        </div>
  
        {isAdmin ? (
          renderAdminPanel()
        ) : (
          <>
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
          </>
        )}

        {selectedLicense && !isAdmin && (
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
                    <h3>Треки:</h3>
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

        {showCreatePlaylistModal && (
          <div className="modal" onClick={() => setShowCreatePlaylistModal(false)}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
              <div className="modal-header">
                <h2>Создание плейлиста</h2>
                <button className="close-button" onClick={() => setShowCreatePlaylistModal(false)}>×</button>
              </div>
              <div className="modal-body">
                <div className="form-group">
                  <label>Название плейлиста:</label>
                  <input
                    type="text"
                    value={newPlaylist.name}
                    onChange={(e) => setNewPlaylist(prev => ({ ...prev, name: e.target.value }))}
                    placeholder="Введите название плейлиста"
                  />
                </div>
                <div className="form-group">
                  <label>Описание:</label>
                  <textarea
                    value={newPlaylist.description}
                    onChange={(e) => setNewPlaylist(prev => ({ ...prev, description: e.target.value }))}
                    placeholder="Введите описание плейлиста"
                  />
                </div>
                <div className="form-group">
                  <label>Выберите треки:</label>
                  <div className="tracks-grid">
                    {adminData.tracks.map(track => (
                      <div
                        key={track.trackId}
                        className={`track-item ${newPlaylist.trackIds.includes(track.trackId) ? 'selected' : ''}`}
                        onClick={() => handleTrackSelect(track.trackId)}
                      >
                        <div className="track-info">
                          <h4>{track.title}</h4>
                          <p>{track.artist}</p>
                          <p className="duration">{formatDuration(track.duration)}</p>
                          <p className="price">${track.price || '0.00'}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
                <div className="form-group">
                  <label>Сумма выбранных треков: ${calculateTotalPrice().toFixed(2)}</label>
                </div>
                <div className="form-group">
                  <label>Цена плейлиста:</label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    value={newPlaylist.price}
                    onChange={(e) => setNewPlaylist(prev => ({ ...prev, price: e.target.value }))}
                    placeholder="Введите цену плейлиста"
                  />
                </div>
                <div className="modal-footer">
                  <button 
                    className="action-button"
                    onClick={handleCreatePlaylist}
                    disabled={!newPlaylist.name || newPlaylist.trackIds.length === 0}
                  >
                    Создать
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    );
  };

export default Profile;

