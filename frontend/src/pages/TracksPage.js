import React, { useState, useEffect } from 'react';
import api from '../api';
import TrackList from '../components/TrackList';
import './TracksPage.css';

const TracksPage = () => {
  const [search, setSearch] = useState('');
  const [sort, setSort] = useState('title');
  const [tracks, setTracks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    api.get('/tracks')
      .then(response => {
        setTracks(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Ошибка загрузки треков:', error);
        setLoading(false);
      });
  }, []);

  const handleBuy = async (priceId) => {
    try {
      const response = await api.post('/licenses/buy', null, { 
        params: { priceId } 
      });
      
      console.log('Покупка успешна:', response.data);
      alert('Покупка успешна!');
      
    } catch (error) {
      console.error('Ошибка покупки:', error);
      alert('Ошибка покупки. Пожалуйста, попробуйте снова.');
    }
  };

  const filteredTracks = tracks
    .filter(track => 
      track.title.toLowerCase().includes(search.toLowerCase()) ||
      track.artist.toLowerCase().includes(search.toLowerCase())
    )
    .sort((a, b) => {
      if (sort === 'title') return a.title.localeCompare(b.title);
      if (sort === 'artist') return a.artist.localeCompare(b.artist);
      if (sort === 'price-asc') return a.price - b.price;
      if (sort === 'price-desc') return b.price - a.price;
      return 0;
    });

  return (
    <div className="catalog-page">
      <h1>Треки</h1>
      <div className="controls">
        <input 
          type="text" 
          placeholder="Поиск треков..." 
          value={search} 
          onChange={(e) => setSearch(e.target.value)}
        />
        <select value={sort} onChange={(e) => setSort(e.target.value)}>
          <option value="title">Сортировать по названию</option>
          <option value="artist">Сортировать по исполнителю</option>
          <option value="price-asc">Цена: по возрастанию</option>
          <option value="price-desc">Цена: по убыванию</option>
        </select>
      </div>
      <TrackList 
        tracks={filteredTracks}
        loading={loading}
        onBuy={handleBuy}
      />
    </div>
  );
};

export default TracksPage;