import React, { useState, useEffect } from 'react';
import api from '../api';
import PlaylistList from '../components/PlaylistList';

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
        console.error('Error fetching playlists:', error);
        setLoading(false);
      });
  }, []);

  const handleBuy = (priceId) => {
    // Логика покупки
    console.log('Buy requested for priceId:', priceId);
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
      <div className="controls">
        <input 
          type="text" 
          placeholder="Search playlists..." 
          value={search} 
          onChange={(e) => setSearch(e.target.value)}
        />
        <select value={sort} onChange={(e) => setSort(e.target.value)}>
          <option value="name">Sort by Title</option>
          <option value="price-asc">Price: Low to High</option>
          <option value="price-desc">Price: High to Low</option>
        </select>
      </div>
      <PlaylistList 
        playlists={filteredPlaylists}
        loading={loading}
        onBuy={handleBuy}
      />
    </div>
  );
};

export default PlaylistsPage;