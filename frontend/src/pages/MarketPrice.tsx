import React, { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, } from 'recharts';

interface DataEntry {
    date: string;
    centsPerKwh: number;
    centsPerKwhWithVat: number;
    eurPerMwh: number;
    eurPerMwhWithVat: number;
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

            const response = await fetch(`http://localhost:8080/api/fetch-elering-date-year`);

            if (!response.ok) {
            throw new Error('Failed to fetch data from the API.');
            }

            const result = await response.json();

            const transformedData = result.map((entry: any) => ({
                date: entry.date,
                centsPerKwh: parseFloat(entry.centsPerKwh.toFixed(2)),
                centsPerKwhWithVat: parseFloat(entry.centsPerKwhWithVat.toFixed(2)),
                eurPerMwh: parseFloat(entry.eurPerMwh.toFixed(2)),
                eurPerMwhWithVat: parseFloat(entry.eurPerMwhWithVat.toFixed(2)),
            }));

            const sortedData = transformedData.sort((a: { date: string | number | Date; }, b: { date: string | number | Date; }) => new Date(a.date).getTime() - new Date(b.date).getTime());

            setData(sortedData);
        } catch (error: any) {
            setError(error.message || 'An unexpected error occurred.');
        } finally {
            setLoading(false);
        }
        };

        fetchData();
    }, []);

    if (loading) {
        return <div className="bg-slate-900 text-white">Loading...</div>;
    }


    return (
        <div>
            <div className="p-5 bg-slate-900 rounded-3xl">
                <h1 className="text-2xl font-bold text-white mb-4">Kuu keskmine Elektri hind sent kWh kohta</h1>
                <ResponsiveContainer width="100%" height={400}>
                    <BarChart data={data} margin={{ top: 5, right: 30, left: 20, bottom: 5, }} >
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="date" tick={{ fill: 'white' }} />
                    <YAxis tick={{ fill: 'white' }} />
                    <Tooltip
                        contentStyle={{ backgroundColor: '#0f172a', border: '2px solid #334155'}}
                        formatter={(value: number) => [`${value} senti kWh kohta`, 'hind']}
                    />
                    <Bar dataKey="centsPerKwh" fill="#047857" />
                    </BarChart>
                </ResponsiveContainer>
            </div>

            <div className="p-5 bg-slate-900 rounded-3xl  mt-10">
                <h1 className="text-xl font-bold text-white mb-4">Kuu keskmine Elektri hind sent kWh kohta koos käibemaksuga</h1>
                <ResponsiveContainer width="100%" height={400}>
                    <BarChart data={data} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="date" tick={{ fill: 'white' }} />
                        <YAxis tick={{ fill: 'white' }} />
                        <Tooltip
                            contentStyle={{ backgroundColor: '#0f172a', border: '2px solid #334155' }}
                            formatter={(value: number) => [`${value} senti kWh kohta`, 'hind']}
                        />
                        <Bar dataKey="centsPerKwhWithVat" fill="#2563eb" />
                    </BarChart>
                </ResponsiveContainer>
            </div>

            <div className="p-5 bg-slate-900 rounded-3xl mt-10">
                <h1 className="text-xl font-bold text-white mb-4">Kuu keskmine Elektri hind EUR/MWh</h1>
                <ResponsiveContainer width="100%" height={400}>
                    <BarChart data={data} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="date" tick={{ fill: 'white' }} />
                        <YAxis tick={{ fill: 'white' }} />
                        <Tooltip
                            contentStyle={{ backgroundColor: '#0f172a', border: '2px solid #334155' }}
                            formatter={(value: number) => [`${value} EUR/MWh`, 'hind']}
                        />
                        <Bar dataKey="eurPerMwh" fill="#9333ea" />
                    </BarChart>
                </ResponsiveContainer>
            </div>

            <div className="p-5 bg-slate-900 rounded-3xl mt-10">
                <h1 className="text-xl font-bold text-white mb-4">Kuu keskmine Elektri hind EUR/MWh koos käibemaksuga</h1>
                <ResponsiveContainer width="100%" height={400}>
                    <BarChart data={data} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="date" tick={{ fill: 'white' }} />
                        <YAxis tick={{ fill: 'white' }} />
                        <Tooltip
                            contentStyle={{ backgroundColor: '#0f172a', border: '2px solid #334155' }}
                            formatter={(value: number) => [`${value} EUR/MWh`, 'hind']}
                        />
                        <Bar dataKey="eurPerMwhWithVat" fill="#f97316" />
                    </BarChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default Overview;
