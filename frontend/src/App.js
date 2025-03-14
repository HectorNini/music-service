// src/App.jsx
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Navigation from './components/Navigation';
import Home from './pages/Home';
import TracksPage from './pages/TracksPage';
import PlaylistsPage from './pages/PlaylistsPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProfilePage from './pages/ProfilePage';
import PrivateRoute from './components/PrivateRoute';
import './styles/global.css';

const App = () => {
  return (
    <Router> {/* Сначала Router */}
      <AuthProvider> {/* Потом AuthProvider */}
        <div className="app-container">
          <Navigation />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/tracks" element={<TracksPage />} />
            <Route path="/playlists" element={<PlaylistsPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route 
              path="/profile" 
              element={
                <PrivateRoute>
                  <ProfilePage />
                </PrivateRoute>
              } 
            />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
};

export default App;