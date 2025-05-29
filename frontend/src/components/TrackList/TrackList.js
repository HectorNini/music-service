import React from 'react';
import './TrackList.css';

const TrackList = ({ tracks, loading, onBuy }) => {
  if (loading) return <div>Загрузка...</div>;

  const formatDuration = (duration) => {
    if (!duration && duration !== 0) return '--:--';
    const minutes = Math.floor(duration / 60);
    const seconds = duration % 60;
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  };

  return (
    <div className="track-list">
      {tracks.map(track => (
        <div key={track.trackId} className="track-item">
          <div className="track-info">
            <h3>{track.title || 'Без названия'}</h3>
            <p>{track.artist || 'Неизвестный исполнитель'}</p>
            <p className="duration">
              {formatDuration(track.duration)}
            </p>
          </div>
          <div className="price-info">
            <span>Цена: ${track.price || '0.00'}</span>
          </div>
          <button onClick={() => onBuy(track.priceId)}>
            Купить
          </button>
        </div>
      ))}
    </div>
  );
};

export default TrackList;