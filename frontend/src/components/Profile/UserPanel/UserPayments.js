import React from 'react';


const UserPayments = ({ payments }) => (
  <div className="payments">
    <h3 className="section-heading">Платежи</h3>
    <div className="items-list">
      {payments.length > 0 ? (
        payments.map(payment => (
          <div key={payment.paymentId} className="payment-item">
            <p><strong>Товар:</strong> {payment.productDescription}</p>
            <p><strong>Сумма:</strong> ${payment.amount}</p>
            <p><strong>Дата:</strong> {new Date(payment.paymentDate).toLocaleDateString()}</p>
          </div>
        ))
      ) : (
        <div>Нет платежей</div>
      )}
    </div>
  </div>
);

export default UserPayments; 