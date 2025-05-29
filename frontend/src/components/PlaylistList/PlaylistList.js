import React, { useState } from 'react';
import './PlaylistList.css';

const PlaylistList = ({ playlists, loading, onBuy }) => {
  const [selectedPlaylist, setSelectedPlaylist] = useState(null);

  const formatDuration = (duration) => {
    if (!duration && duration !== 0) return '--:--';
    const minutes = Math.floor(duration / 60);
    const seconds = duration % 60;
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  };

  if (loading) return <div>Загрузка...</div>;

  return (
    <div className="playlist-list">
      {playlists.map(playlist => (
        <div key={playlist.playlistId} className="playlist-item">
          <h3 onClick={() => setSelectedPlaylist(playlist)} style={{ cursor: 'pointer' }}>
            {playlist.name}
          </h3>
          <p>{playlist.description}</p>
          <div className="price-info">
            <span>Цена: ${playlist.price || '0.00'}</span>
          </div>
          <button onClick={() => onBuy(playlist.priceId)}>
            Купить
          </button>
        </div>
      ))}

      {selectedPlaylist && (
        <div className="modal" onClick={() => setSelectedPlaylist(null)}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{selectedPlaylist.name}</h2>
              <button className="close-button" onClick={() => setSelectedPlaylist(null)}>×</button>
            </div>
            <p>{selectedPlaylist.description}</p>
            <div className="tracks-list">
              <h3>Треки в плейлисте:</h3>
              <div className="tracks-grid">
                {selectedPlaylist.tracks.map(track => (
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
          </div>
        </div>
      )}
    </div>
  );
};

export default PlaylistList; 