import React, { useEffect, useState } from 'react';
import { Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import api from '../api';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import '../styles/fonts/Roboto-Regular-normal.js';

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
);

const AdminPanel = () => {
    const [topTracks, setTopTracks] = useState([]);
    const [topPlaylists, setTopPlaylists] = useState([]);
    const [userCount, setUserCount] = useState(0);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [tracksRes, playlistsRes, userCountRes] = await Promise.all([
                    api.get('/statistics/top-tracks'),
                    api.get('/statistics/top-playlists'),
                    api.get('/statistics/user-count')
                ]);

                setTopTracks(tracksRes.data);
                setTopPlaylists(playlistsRes.data);
                setUserCount(userCountRes.data);
            } catch (error) {
                console.error('Ошибка при загрузке статистики:', error);
            }
        };

        fetchData();
    }, []);

    const exportToPDF = () => {
        const doc = new jsPDF();
        doc.setFont('Roboto-Regular', 'normal');
        doc.setFontSize(20);
        doc.text('Панель администратора', 14, 15);

        doc.setFontSize(14);
        doc.text(`Общее количество пользователей: ${userCount}`, 14, 30);

        doc.text('Топ 10 треков по количеству покупок:', 14, 45);
        autoTable(doc, {
            startY: 50,
            head: [['Название трека', 'Количество покупок']],
            body: topTracks.map(track => [track.title, track.count]),
            theme: 'grid',
            headStyles: { fillColor: [75, 192, 192], font: 'Roboto-Regular', fontStyle: 'normal' },
            styles: { font: 'Roboto-Regular', fontStyle: 'normal' },
            columnStyles: { 0: { font: 'Roboto-Regular', fontStyle: 'normal' }, 1: { font: 'Roboto-Regular', fontStyle: 'normal' } }
        });

        doc.text('Топ 10 плейлистов по количеству покупок:', 14, doc.lastAutoTable.finalY + 15);
        autoTable(doc, {
            startY: doc.lastAutoTable.finalY + 20,
            head: [['Название плейлиста', 'Количество покупок']],
            body: topPlaylists.map(playlist => [playlist.name, playlist.count]),
            theme: 'grid',
            headStyles: { fillColor: [153, 102, 255], font: 'Roboto-Regular', fontStyle: 'normal' },
            styles: { font: 'Roboto-Regular', fontStyle: 'normal' },
            columnStyles: { 0: { font: 'Roboto-Regular', fontStyle: 'normal' }, 1: { font: 'Roboto-Regular', fontStyle: 'normal' } }
        });

        doc.save('statistics-report.pdf');
    };

    const tracksChartData = {
        labels: topTracks.map(item => item.title),
        datasets: [
            {
                label: 'Количество покупок',
                data: topTracks.map(item => item.count),
                backgroundColor: 'rgba(75, 192, 192, 0.6)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
            },
        ],
    };

    const playlistsChartData = {
        labels: topPlaylists.map(item => item.name),
        datasets: [
            {
                label: 'Количество покупок',
                data: topPlaylists.map(item => item.count),
                backgroundColor: 'rgba(153, 102, 255, 0.6)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 1,
            },
        ],
    };

    const chartOptions = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Топ покупок',
            },
        },
    };

    return (
        <div className="admin-panel-container">
            <div className="admin-panel-header">
                <h1>Панель администратора</h1>
                <button 
                    onClick={exportToPDF}
                    className="export-button"
                >
                    Экспорт в PDF
                </button>
            </div>
            
            <div className="user-count-section">
                <h2>Общее количество пользователей: {userCount}</h2>
            </div>

            <div className="charts-section">
                <div className="chart-box">
                    <h2>Топ треков</h2>
                    <Bar data={tracksChartData} options={chartOptions} />
                </div>

                <div className="chart-box">
                    <h2>Топ плейлистов</h2>
                    <Bar data={playlistsChartData} options={chartOptions} />
                </div>
            </div>
        </div>
    );
};

export default AdminPanel; 