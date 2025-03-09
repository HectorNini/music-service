import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Register from './components/Register'; // Правильный путь
import Login from './components/Login';       // Правильный путь
import Home from './components/Home';         // Правильный путь

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Home />} />
      </Routes>
    </Router>
  );
}

export default App;