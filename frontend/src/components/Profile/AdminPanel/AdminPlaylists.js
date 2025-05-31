import React from 'react';

const AdminPlaylists = ({ playlists, tracks, handlers, modals, state, formatDuration }) => (
  <div className="admin-section mb-30">
    <h3>Управление плейлистами</h3>
    <div className="admin-actions mb-20 d-flex gap-10">
      <button 
        className="action-button"
        onClick={handlers.openCreatePlaylistModal}
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
          {playlists.map(playlist => (
            <tr key={playlist.playlistId}>
              <td>{playlist.playlistId}</td>
              <td>{playlist.name}</td>
              <td>{playlist.description}</td>
              <td>{playlist.tracks?.length || 0}</td>
              <td>${playlist.price}</td>
              <td>
                <div className="admin-table-actions">
                  <button
                    className="action-button delete-button"
                    onClick={() => handlers.deletePlaylist(playlist.playlistId)}
                  >
                    Удалить
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    {modals.showCreatePlaylistModal && modals.CreatePlaylistModal}
  </div>
);

export default AdminPlaylists; 