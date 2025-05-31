import React from 'react';

const AdminTracks = ({ tracks, formatDuration, handlers, modals, state }) => (
  <div className="admin-section mb-30">
    <h3>Управление треками</h3>
    <div className="admin-actions mb-20 d-flex gap-10">
      <button 
        className="action-button"
        onClick={handlers.openCreateTrackModal}
      >
        Добавить трек
      </button>
    </div>
    <div className="admin-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Название</th>
            <th>Исполнитель</th>
            <th>Длительность</th>
            <th>Цена</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {tracks.map(track => (
            <tr key={track.trackId}>
              <td>{track.trackId}</td>
              <td>{track.title}</td>
              <td>{track.artist}</td>
              <td>{formatDuration(track.duration)}</td>
              <td>${track.price}</td>
              <td>
                <div className="admin-table-actions">
                  <button
                    className="action-button"
                    onClick={() => handlers.editTrack(track)}
                  >
                    Редактировать
                  </button>
                  <button
                    className="action-button delete-button"
                    onClick={() => handlers.deleteTrack(track.trackId)}
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
    {modals.showCreateTrackModal && modals.CreateTrackModal}
  </div>
);

export default AdminTracks; 