import React, { useState } from 'react';
import LicenseDurationModal from '../Profile/LicenseDurationModal';

const TrackList = ({ tracks, loading, onBuy }) => {
  const [selectedTrack, setSelectedTrack] = useState(null);

  const formatDuration = (duration) => {
    if (!duration && duration !== 0) return '--:--';
    const minutes = Math.floor(duration / 60);
    const seconds = duration % 60;
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  };

  const handleBuyClick = (track) => {
    setSelectedTrack(track);
  };

  const handleDurationSelect = (months) => {
    if (selectedTrack) {
      onBuy(selectedTrack.priceId, months);
      setSelectedTrack(null);
    }
  };

  if (loading) return <div>Загрузка...</div>;

  return (
    <div className="track-list p-20 gap-20">
      {tracks.map(track => (
        <div key={track.trackId} className="track-item d-flex flex-direction-column p-15">
          <div className="track-info">
            <h3 className="mb-10">{track.title || 'Без названия'}</h3>
            <p className="my-5">{track.artist || 'Неизвестный исполнитель'}</p>
            <p className="duration">
              {formatDuration(track.duration)}
            </p>
          </div>
          <div className="price-info my-10">
            <span>Цена: ${track.price || '0.00'}</span>
          </div>
          <button onClick={() => handleBuyClick(track)}>
            Купить
          </button>
        </div>
      ))}

      {selectedTrack && (
        <LicenseDurationModal
          onClose={() => setSelectedTrack(null)}
          onSelect={handleDurationSelect}
          basePrice={selectedTrack.price}
        />
      )}
    </div>
  );
};

export default TrackList;