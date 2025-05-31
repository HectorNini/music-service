import React, { useState, useEffect } from 'react';
import api from '../api';
import PlaylistList from '../components/PlaylistList/PlaylistList';
import CatalogControls from '../components/CatalogControls/CatalogControls';

const PlaylistsPage = () => {
  const [search, setSearch] = useState('');
  const [sort, setSort] = useState('name');
  const [playlists, setPlaylists] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    api.get('/playlists')
      .then(response => {
        setPlaylists(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Ошибка загрузки плейлистов:', error);
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

  const filteredPlaylists = playlists
    .filter(playlist => 
      playlist.name.toLowerCase().includes(search.toLowerCase()) ||
      (playlist.description && playlist.description.toLowerCase().includes(search.toLowerCase()))
    )
    .sort((a, b) => {
      if (sort === 'name') return a.name.localeCompare(b.name);
      if (sort === 'price-asc') return a.price - b.price;
      if (sort === 'price-desc') return b.price - a.price;
      return 0;
    });

  return (
    <div className="catalog-page">
      <h1>Плейлисты</h1>
      <CatalogControls
        search={search}
        setSearch={setSearch}
        sort={sort}
        setSort={setSort}
        placeholder="Поиск плейлистов..."
        sortOptions={[
          { value: 'name', label: 'Сортировать по названию' },
          { value: 'price-asc', label: 'Цена: по возрастанию' },
          { value: 'price-desc', label: 'Цена: по убыванию' },
        ]}
      />
      <PlaylistList 
        playlists={filteredPlaylists}
        loading={loading}
        onBuy={handleBuy}
      />
    </div>
  );
};

export default PlaylistsPage;