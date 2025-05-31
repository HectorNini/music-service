import React from 'react';

const AdminPayments = ({ payments }) => (
  <div className="admin-section mb-30">
    <h3>Управление платежами</h3>
    <div className="admin-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Товар</th>
            <th>Сумма</th>
            <th>Дата</th>
            <th>Статус</th>
            <th>Метод оплаты</th>
          </tr>
        </thead>
        <tbody>
          {payments.map(payment => (
            <tr key={payment.paymentId}>
              <td>{payment.paymentId}</td>
              <td>{payment.productDescription}</td>
              <td>${payment.amount}</td>
              <td>{new Date(payment.paymentDate).toLocaleDateString()}</td>
              <td>
                <span className={`status-${payment.status.toLowerCase()}`}>
                  {payment.status}
                </span>
              </td>
              <td>
                <span className="payment-method">
                  {payment.paymentMethod}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
);

export default AdminPayments; 