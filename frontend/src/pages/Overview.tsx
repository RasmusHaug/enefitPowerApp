import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const data = [
    {
        name: 'Esmaspäev',
        elektri_hind: 4000,
    },
    {
        name: 'Teisipäev',
        elektri_hind: 4000,
    },
    {
        name: 'Kolmapäev',
        elektri_hind: 4000,
    },
    {
        name: 'Neljapäev',
        elektri_hind: 4000,
    },
    {
        name: 'Reede',
        elektri_hind: 4000,
    },
    {
        name: 'Laupäev',
        elektri_hind: 4000,
    },
    {
        name: 'Pühapäev',
        elektri_hind: 4000,
    },
];

const Overview: React.FC = () => {
    return (
        <div className="p-5 bg-slate-900 rounded-md">
            <h1 className="text-2xl font-bold text-white mb-4">Elektri Hind viimane nädal</h1>
            <ResponsiveContainer width="100%" height={300}>
                <BarChart data={data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                {/* Bar for elektri_hind */}
                <Bar dataKey="elektri_hind" fill="#4c51bf" />
                {/* Bar for elektri_kulu */}
                <Bar dataKey="elektri_kulu" fill="#63b3ed" />
                </BarChart>
            </ResponsiveContainer>
    </div>
    );
};

export default Overview;