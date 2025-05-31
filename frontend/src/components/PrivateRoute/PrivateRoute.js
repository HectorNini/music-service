// src/components/PrivateRoute.jsx
import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

const PrivateRoute = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      navigate('/login', {
        state: { from: location },
        replace: true
      });
    }
  }, [isAuthenticated, isLoading, navigate, location]);

  return (isAuthenticated && !isLoading) ? children : null;
};

export default PrivateRoute; 