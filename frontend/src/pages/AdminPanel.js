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
import '../fonts/Roboto-Regular-normal.js';

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
        <div className="container mx-auto p-4">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Панель администратора</h1>
                <button 
                    onClick={exportToPDF}
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                >
                    Экспорт в PDF
                </button>
            </div>
            
            <div className="mb-8">
                <h2 className="text-xl font-semibold mb-4">Общее количество пользователей</h2>
                <div className="bg-white p-4 rounded-lg shadow">
                    <p className="text-3xl font-bold">{userCount}</p>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <div className="bg-white p-4 rounded-lg shadow">
                    <h2 className="text-xl font-semibold mb-4">Топ треков</h2>
                    <Bar data={tracksChartData} options={chartOptions} />
                </div>

                <div className="bg-white p-4 rounded-lg shadow">
                    <h2 className="text-xl font-semibold mb-4">Топ плейлистов</h2>
                    <Bar data={playlistsChartData} options={chartOptions} />
                </div>
            </div>
        </div>
    );
};

export default AdminPanel; 