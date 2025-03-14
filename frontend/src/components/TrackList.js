import React from 'react';

const TrackList = ({ tracks, loading, onBuy }) => {
  if (loading) return <div>Loading...</div>;

  return (
    <div className="track-list">
      {tracks.map(track => (
        <div key={track.trackId} className="track-item">
          <h3>{track.title}</h3>
          <p>Artist: {track.artist}</p>
          <p>Price: ${track.price}</p>
          <button onClick={() => onBuy(track.priceId)}>
            Buy License
          </button>
        </div>
      ))}
    </div>
  );
};

export default TrackList;