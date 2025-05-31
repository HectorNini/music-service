const handleBuy = async (priceId, months) => {
  try {
    const response = await api.post(`/licenses/buy?priceId=${priceId}&months=${months}`);
    if (response.status === 200) {
      // Обновляем список лицензий после успешной покупки
      const licensesResponse = await api.get('/licenses/active');
      setLicenses(licensesResponse.data);
      // Показываем уведомление об успешной покупке
      alert('Лицензия успешно приобретена!');
    }
  } catch (error) {
    console.error('Ошибка при покупке лицензии:', error);
    alert('Произошла ошибка при покупке лицензии. Пожалуйста, попробуйте позже.');
  }
}; 