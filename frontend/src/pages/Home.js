// src/pages/Home.jsx
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div className="home-container">
      <section className="intro-section">
        <h1 className="home-heading">Добро пожаловать в Music Service</h1>
        <p>Откройте мир музыки: слушайте, лицензируйте и управляйте вашей фонотекой с легкостью.</p>
        <div className="cta-buttons">
          <Link to="/tracks" className="cta-button">
            Изучить треки
          </Link>
          <Link to="/playlists" className="cta-button">
            Изучить плейлисты
          </Link>
        </div>
      </section>
    </div>
  );
};


export default Home;