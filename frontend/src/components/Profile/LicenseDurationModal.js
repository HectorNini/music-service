import React from 'react';
import './Profile.css';

const LicenseDurationModal = ({ onClose, onSelect, basePrice }) => {
  // Расчет цен для разных периодов с учетом скидок
  const calculatePrice = (months) => {
    const discountRates = {
      1: 1,      // 1 месяц - базовая цена
      3: 0.9,    // 3 месяца - 10% скидка
      6: 0.85,   // 6 месяцев - 15% скидка
      12: 0.8    // 12 месяцев - 20% скидка
    };
    return (basePrice * months * discountRates[months]).toFixed(2);
  };

  const durationOptions = [
    { months: 1, label: '1 месяц' },
    { months: 3, label: '3 месяца' },
    { months: 6, label: '6 месяцев' },
    { months: 12, label: '1 год' }
  ];

  return (
    <div className="modal" onClick={onClose}>
      <div className="modal-content" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Выберите срок лицензии</h2>
          <button className="close-button" onClick={onClose}>×</button>
        </div>
        <div className="license-duration-options">
          {durationOptions.map(option => (
            <div 
              key={option.months} 
              className="duration-option"
              onClick={() => onSelect(option.months)}
            >
              <div className="duration-label">{option.label}</div>
              <div className="duration-price">
                <span className="price">${calculatePrice(option.months)}</span>
                {option.months > 1 && (
                  <span className="monthly-price">
                    (${(calculatePrice(option.months) / option.months).toFixed(2)}/мес)
                  </span>
                )}
              </div>
              {option.months > 1 && (
                <div className="discount-badge">
                  {option.months === 3 && 'Скидка 10%'}
                  {option.months === 6 && 'Скидка 15%'}
                  {option.months === 12 && 'Скидка 20%'}
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default LicenseDurationModal; 