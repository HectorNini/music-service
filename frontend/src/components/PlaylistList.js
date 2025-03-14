import React from 'react';

const PlaylistList = ({ playlists, loading, onBuy }) => {
  if (loading) return <div>Loading...</div>;

  return (
    <div className="playlist-list">
      {playlists.map(playlist => (
        <div key={playlist.playlistId} className="playlist-item">
          <h3>{playlist.name}</h3>
          <p>{playlist.description}</p>
          <button onClick={() => onBuy(playlist.priceId)}>
            Buy License (${playlist.price})
          </button>
        </div>
      ))}
    </div>
  );
};

export default PlaylistList;