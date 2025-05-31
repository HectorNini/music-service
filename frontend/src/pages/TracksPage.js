import React, { useState, useEffect } from 'react';
import api from '../api';
import TrackList from '../components/TrackList/TrackList';
import CatalogControls from '../components/CatalogControls/CatalogControls';

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

  const handleBuy = async (priceId, months) => {
    try {
      const response = await api.post('/licenses/buy', null, { 
        params: { priceId, months } 
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
      <CatalogControls
        search={search}
        setSearch={setSearch}
        sort={sort}
        setSort={setSort}
        placeholder="Поиск треков..."
        sortOptions={[
          { value: 'title', label: 'Сортировать по названию' },
          { value: 'artist', label: 'Сортировать по исполнителю' },
          { value: 'price-asc', label: 'Цена: по возрастанию' },
          { value: 'price-desc', label: 'Цена: по убыванию' },
        ]}
      />
      <TrackList 
        tracks={filteredTracks}
        loading={loading}
        onBuy={handleBuy}
      />
    </div>
  );
};

export default TracksPage;