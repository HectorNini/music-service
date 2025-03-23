import React, { useState, useEffect } from 'react';
import api from '../api';
import TrackList from '../components/TrackList';

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
        console.error('Error fetching tracks:', error);
        setLoading(false);
      });
  }, []);

  const handleBuy = async (priceId) => {
    try {
      const response = await api.post('/licenses/buy', null, { 
        params: { priceId } 
      });
      
      // Обработка успешной покупки
      console.log('Purchase successful:', response.data);
      alert('Purchase successful!');
      
      // Обновление данных после покупки (если нужно)
      // Например, можно перезагрузить список лицензий или обновить UI
      
    } catch (error) {
      console.error('Purchase failed:', error);
      alert('Purchase failed. Please try again.');
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
      <div className="controls">
        <input 
          type="text" 
          placeholder="Search tracks..." 
          value={search} 
          onChange={(e) => setSearch(e.target.value)}
        />
        <select value={sort} onChange={(e) => setSort(e.target.value)}>
          <option value="title">Sort by Title</option>
          <option value="artist">Sort by Artist</option>
          <option value="price-asc">Price: Low to High</option>
          <option value="price-desc">Price: High to Low</option>
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