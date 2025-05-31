import React, { useState, useEffect } from 'react';
import api from '../../../api';
import AdminUsers from './AdminUsers';
import AdminTracks from './AdminTracks';
import AdminPlaylists from './AdminPlaylists';
import AdminLicenses from './AdminLicenses';
import AdminPayments from './AdminPayments';

const AdminPanel = ({ user, formatDuration }) => {
  const [adminData, setAdminData] = useState({
    users: [],
    tracks: [],
    playlists: [],
    licenses: [],
    payments: []
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('users');

  // Состояния для модалок и редактирования
  const [showCreateTrackModal, setShowCreateTrackModal] = useState(false);
  const [showCreatePlaylistModal, setShowCreatePlaylistModal] = useState(false);
  const [selectedTrack, setSelectedTrack] = useState(null);
  const [newTrack, setNewTrack] = useState({ title: '', artist: '', duration: '', price: '', filePath: '' });
  const [newPlaylist, setNewPlaylist] = useState({ name: '', description: '', trackIds: [], price: '' });

  useEffect(() => {
    const fetchAdminData = async (endpoint) => {
      try {
        setLoading(true);
        const response = await api.get(`/${endpoint}`);
        return response.data;
      } catch (err) {
        setError(`Ошибка при загрузке ${endpoint}: ${err.response?.data || err.message}`);
        return [];
      } finally {
        setLoading(false);
      }
    };

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
          setAdminData({ tracks, playlists, payments, users, licenses });
        } catch (err) {
          setError('Ошибка при загрузке данных администратора');
        }
      }
    };
    loadAdminData();
  }, [user]);

  // Обработчики для треков
  const openCreateTrackModal = () => {
    setShowCreateTrackModal(true);
    setSelectedTrack(null);
    setNewTrack({ title: '', artist: '', duration: '', price: '', filePath: '' });
  };
  const editTrack = (track) => {
    setSelectedTrack(track);
    setNewTrack({
      title: track.title,
      artist: track.artist,
      duration: track.duration,
      price: track.pricing?.price || track.price || '',
      filePath: track.filePath || ''
    });
    setShowCreateTrackModal(true);
  };
  const deleteTrack = async (trackId) => {
    if (window.confirm('Вы уверены, что хотите удалить этот трек?')) {
      setLoading(true);
      await api.delete(`/tracks/${trackId}`);
      const updatedTracks = await api.get('/tracks');
      setAdminData(prev => ({ ...prev, tracks: updatedTracks.data }));
      setLoading(false);
    }
  };
  const handleTrackModalSave = async () => {
    setLoading(true);
    if (selectedTrack) {
      await api.put(`/tracks/${selectedTrack.trackId}`, {
        ...newTrack,
        price: newTrack.price ? parseFloat(newTrack.price) : null
      });
    } else {
      await api.post('/tracks', {
        ...newTrack,
        price: newTrack.price ? parseFloat(newTrack.price) : null
      });
    }
    const updatedTracks = await api.get('/tracks');
    setAdminData(prev => ({ ...prev, tracks: updatedTracks.data }));
    setShowCreateTrackModal(false);
    setSelectedTrack(null);
    setNewTrack({ title: '', artist: '', duration: '', price: '', filePath: '' });
    setLoading(false);
  };

  // Обработчики для плейлистов
  const openCreatePlaylistModal = () => {
    setShowCreatePlaylistModal(true);
    setNewPlaylist({ name: '', description: '', trackIds: [], price: '' });
  };
  const deletePlaylist = async (playlistId) => {
    if (window.confirm('Вы уверены, что хотите удалить этот плейлист?')) {
      setLoading(true);
      await api.delete(`/playlists/${playlistId}`);
      setAdminData(prev => ({ ...prev, playlists: prev.playlists.filter(p => p.playlistId !== playlistId) }));
      setLoading(false);
    }
  };
  const handlePlaylistModalSave = async () => {
    setLoading(true);
    await api.post('/playlists', {
      ...newPlaylist,
      price: newPlaylist.price ? parseFloat(newPlaylist.price) : null
    });
    const updatedPlaylists = await api.get('/playlists');
    setAdminData(prev => ({ ...prev, playlists: updatedPlaylists.data }));
    setShowCreatePlaylistModal(false);
    setNewPlaylist({ name: '', description: '', trackIds: [], price: '' });
    setLoading(false);
  };

  // Модалки
  const modals = {
    showCreateTrackModal,
    CreateTrackModal: showCreateTrackModal && (
      <div className="modal" onClick={() => setShowCreateTrackModal(false)}>
        <div className="modal-content" onClick={e => e.stopPropagation()}>
          <div className="modal-header">
            <h2>{selectedTrack ? 'Редактирование трека' : 'Создание трека'}</h2>
            <button className="close-button" onClick={() => setShowCreateTrackModal(false)}>×</button>
          </div>
          <div className="modal-body">
            <div className="form-group">
              <label>Название:</label>
              <input type="text" value={newTrack.title} onChange={e => setNewTrack(prev => ({ ...prev, title: e.target.value }))} placeholder="Введите название трека" />
            </div>
            <div className="form-group">
              <label>Исполнитель:</label>
              <input type="text" value={newTrack.artist} onChange={e => setNewTrack(prev => ({ ...prev, artist: e.target.value }))} placeholder="Введите исполнителя" />
            </div>
            <div className="form-group">
              <label>Длительность:</label>
              <input type="text" value={newTrack.duration} onChange={e => setNewTrack(prev => ({ ...prev, duration: e.target.value }))} placeholder="Введите длительность трека в секундах" />
            </div>
            <div className="form-group">
              <label>Путь к файлу:</label>
              <input type="text" value={newTrack.filePath} onChange={e => setNewTrack(prev => ({ ...prev, filePath: e.target.value }))} placeholder="Введите путь к аудиофайлу" />
            </div>
            <div className="form-group">
              <label>Цена:</label>
              <input type="number" step="0.01" min="0" value={newTrack.price} onChange={e => setNewTrack(prev => ({ ...prev, price: e.target.value }))} placeholder="Введите цену трека" />
            </div>
            <div className="modal-footer">
              <button className="action-button" onClick={handleTrackModalSave} disabled={!newTrack.title || !newTrack.artist || !newTrack.duration || !newTrack.filePath}>{selectedTrack ? 'Сохранить' : 'Создать'}</button>
            </div>
          </div>
        </div>
      </div>
    ),
    showCreatePlaylistModal,
    CreatePlaylistModal: showCreatePlaylistModal && (
      <div className="modal" onClick={() => setShowCreatePlaylistModal(false)}>
        <div className="modal-content" onClick={e => e.stopPropagation()}>
          <div className="modal-header">
            <h2>Создание плейлиста</h2>
            <button className="close-button" onClick={() => setShowCreatePlaylistModal(false)}>×</button>
          </div>
          <div className="modal-body">
            <div className="form-group">
              <label>Название плейлиста:</label>
              <input type="text" value={newPlaylist.name} onChange={e => setNewPlaylist(prev => ({ ...prev, name: e.target.value }))} placeholder="Введите название плейлиста" />
            </div>
            <div className="form-group">
              <label>Описание:</label>
              <textarea value={newPlaylist.description} onChange={e => setNewPlaylist(prev => ({ ...prev, description: e.target.value }))} placeholder="Введите описание плейлиста" />
            </div>
            <div className="form-group">
              <label>Выберите треки:</label>
              <div className="tracks-grid">
                {adminData.tracks.map(track => (
                  <div key={track.trackId} className={`track-item ${newPlaylist.trackIds.includes(track.trackId) ? 'selected' : ''}`} onClick={() => setNewPlaylist(prev => ({ ...prev, trackIds: prev.trackIds.includes(track.trackId) ? prev.trackIds.filter(id => id !== track.trackId) : [...prev.trackIds, track.trackId] }))}>
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
              <label>Сумма выбранных треков: ${adminData.tracks.filter(track => newPlaylist.trackIds.includes(track.trackId)).reduce((sum, track) => sum + (track.price || 0), 0).toFixed(2)}</label>
            </div>
            <div className="form-group">
              <label>Цена плейлиста:</label>
              <input type="number" step="0.01" min="0" value={newPlaylist.price} onChange={e => setNewPlaylist(prev => ({ ...prev, price: e.target.value }))} placeholder="Введите цену плейлиста" />
            </div>
            <div className="modal-footer">
              <button className="action-button" onClick={handlePlaylistModalSave} disabled={!newPlaylist.name || newPlaylist.trackIds.length === 0}>Создать</button>
            </div>
          </div>
        </div>
      </div>
    )
  };

  const handlers = {
    openCreateTrackModal,
    editTrack,
    deleteTrack,
    openCreatePlaylistModal,
    deletePlaylist
  };
  const state = {
    newTrack, setNewTrack, selectedTrack, setSelectedTrack, newPlaylist, setNewPlaylist
  };

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error) return <div className="error-message">{error}</div>;

  return (
    <div className="admin-panel">
      <div className="admin-tabs">
        <button className={`tab-button ${activeTab === 'users' ? 'active' : ''}`} onClick={() => setActiveTab('users')}>Пользователи</button>
        <button className={`tab-button ${activeTab === 'tracks' ? 'active' : ''}`} onClick={() => setActiveTab('tracks')}>Треки</button>
        <button className={`tab-button ${activeTab === 'playlists' ? 'active' : ''}`} onClick={() => setActiveTab('playlists')}>Плейлисты</button>
        <button className={`tab-button ${activeTab === 'licenses' ? 'active' : ''}`} onClick={() => setActiveTab('licenses')}>Лицензии</button>
        <button className={`tab-button ${activeTab === 'payments' ? 'active' : ''}`} onClick={() => setActiveTab('payments')}>Платежи</button>
      </div>
      <div className="admin-content">
        {activeTab === 'users' && <AdminUsers users={adminData.users || []} />}
        {activeTab === 'tracks' && <AdminTracks tracks={adminData.tracks || []} formatDuration={formatDuration} handlers={handlers} modals={modals} state={state} />}
        {activeTab === 'playlists' && <AdminPlaylists playlists={adminData.playlists || []} tracks={adminData.tracks || []} handlers={handlers} modals={modals} state={state} formatDuration={formatDuration} />}
        {activeTab === 'licenses' && <AdminLicenses licenses={adminData.licenses || []} />}
        {activeTab === 'payments' && <AdminPayments payments={adminData.payments || []} />}
      </div>
    </div>
  );
};

export default AdminPanel; 