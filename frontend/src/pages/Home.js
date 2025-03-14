// src/pages/Home.jsx
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div className="home-container">
      <section className="intro-section">
        <h1>Welcome to Music Service</h1>
        <p>Discover and license tracks and playlists</p>
        <div className="cta-buttons">
          <Link to="/tracks" className="cta-button">
            Explore Tracks
          </Link>
          <Link to="/playlists" className="cta-button">
            Explore Playlists
          </Link>
        </div>
      </section>
    </div>
  );
};


export default Home;