import React, { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, } from 'recharts';
import dayjs from 'dayjs';

interface DataEntry {
    date: string;
    centsPerKwh: number;
    centsPerKwhWithVat: number;
    eurPerMvw: number;
    eurPerMvwWithVat: number;
}

const Overview: React.FC = () => {
    const [data, setData] = useState<DataEntry[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchData = async () => {
        try {
            setLoading(true);
            setError(null);

            const endDate = dayjs().format('YYYY-MM-DD');
            const fromDate = dayjs().subtract(7, 'day').format('YYYY-MM-DD');

            const response = await fetch( `http://localhost:8080/api/fetch-elering-date-range?fromDate=${fromDate}&endDate=${endDate}`);

            if (!response.ok) {
            throw new Error('Failed to fetch data from the API.');
            }

            const result = await response.json();

            const transformedData = result.map((entry: any) => ({
                date: entry.date,
                centsPerKwh: entry.centsPerKwh,
                centsPerKwhWithVat: entry.centsPerKwhWithVat,
                eurPerMvw: entry.eurPerMwh,
                eurPerMvwWithVat: entry.eurPerMwhWithVat,
            }));

            setData(transformedData);
        } catch (error: any) {
            setError(error.message || 'An unexpected error occurred.');
        } finally {
            setLoading(false);
        }
        };

        fetchData();
    }, []);

    if (loading) {
        return <div className="text-white">Loading...</div>;
    }

    if (error) {
        return <div className="text-red-500">Error: {error}</div>;
    }

    return (
        <div className="p-5 bg-slate-900 rounded-md">
            <h1 className="text-2xl font-bold text-white mb-4">Päeva keskmine Elektri hind sent KwH kohta (Viimased 7. päeva)</h1>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart
                data={data}
                margin={{
                    top: 5,
                    right: 30,
                    left: 20,
                    bottom: 5,
                }}
                >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" tick={{ fill: 'white' }} />
                <YAxis tick={{ fill: 'white' }} />
                <Tooltip
                    contentStyle={{ backgroundColor: '#0f172a', border: '2px solid #334155'}}
                />
                <Bar dataKey="centsPerKwh" fill="#047857" />
                </BarChart>
            </ResponsiveContainer>
        </div>
    );
};

export default Overview;
