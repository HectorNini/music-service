import React from 'react';

const CatalogControls = ({ search, setSearch, sort, setSort, placeholder, sortOptions }) => {
  return (
    <div className="controls">
      <input 
        type="text" 
        placeholder={placeholder} 
        value={search} 
        onChange={(e) => setSearch(e.target.value)}
      />
      <select value={sort} onChange={(e) => setSort(e.target.value)}>
        {sortOptions.map(option => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
    </div>
  );
};

export default CatalogControls; 