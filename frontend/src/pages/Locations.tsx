import React, { useEffect, useState } from 'react';
import { useUser } from '../CustomerManagement/UserContext';

// TODO: Add option to delete Locations.

interface MeteringPoint {
    id: number;
    name: string;
    address: string;
    city: string;
    postalCode: string;
}

const Locations: React.FC = () => {
    const [meteringPoints, setMeteringPoints] = useState<MeteringPoint[]>([]);
    const [newMeteringPoint, setNewMeteringPoint] = useState({
            name: '',
            address: '',
            city: '',
            postalCode: '',
    });
    const [isAdding, setIsAdding] = useState(false);
    const { user } = useUser();
    const customerId = user?.customerId;
    const token = user?.sessionId;

    const fetchMeteringPoints = async () => {
        try {
            const response = await fetch( `http://localhost:8080/api/customers/${customerId}/get-metering-points`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Failed to fetch metering points');
            }

            const data = await response.json();
            setMeteringPoints(data);
        } catch (error) {
            console.error('Error fetching metering points:', error);
        }
    };

    const handleAddNew = () => {
        setIsAdding(true);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setNewMeteringPoint((prev) => ({
        ...prev,
        [name]: value,
        }));
    };

    const addMeteringPoint = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await fetch( `http://localhost:8080/api/customers/${customerId}/add-metering-points`, {
                method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                    credentials: 'include',
                    body: JSON.stringify(newMeteringPoint),
            });

            if (!response.ok) {
                throw new Error("Failed to add new metering point.");
            }

            const addedPoint = await response.json();
            setMeteringPoints((prev) => [...prev, addedPoint]);
            setNewMeteringPoint({ name: '', address: '', city: '', postalCode: '' });
            setIsAdding(false);
        } catch (error) {
            console.error('Error adding metering point:', error);
        }
    };

    useEffect(() => {
        fetchMeteringPoints();
    }, []);

    return (
        <div className="p-3 bg-slate-900 rounded-md">
            <div className="m-2 p-2 flex justify-between items-center">
                <h1 className="text-2xl font-bold text-white">Salvestatud eluasemed</h1>
                <button
                onClick={handleAddNew}
                className="px-2 py-1 rounded-md bg-green-700 text-white hover:bg-green-500"
                >
                Lisa Uus
                </button>
            </div>

            {isAdding && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-slate-700 rounded-md p-6 w-96 border-4 border-emerald-700">
                        <h2 className="text-lg font-bold mb-4">Lisa uus eluase</h2>
                        <form onSubmit={addMeteringPoint}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium">Eluase Nimetus</label>
                                <input
                                    type="text"
                                    name="name"
                                    value={newMeteringPoint.name}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border rounded-md text-black"
                                    required
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium">Aadress</label>
                                <input
                                    type="text"
                                    name="address"
                                    value={newMeteringPoint.address}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border rounded-md text-black"
                                    required
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium">Linn</label>
                                <input
                                    type="text"
                                    name="city"
                                    value={newMeteringPoint.city}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border rounded-md text-black"
                                    required
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium">Postiindeks</label>
                                <input
                                    type="text"
                                    name="postalCode"
                                    value={newMeteringPoint.postalCode}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border rounded-md text-black"
                                    required
                                />
                            </div>
                            <div className="flex justify-end">
                                <button
                                    type="button"
                                    onClick={() => setIsAdding(false)}
                                    className="px-4 py-2 mr-2 bg-gray-600 rounded-md hover:bg-gray-400"
                                >
                                    TÜHISTA
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-green-700 text-white rounded-md hover:bg-green-500"
                                >
                                    SALVESTA
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                {meteringPoints.map((point) => (
                    <div key={point.id} className="p-4 bg-slate-800 rounded-md">
                        <h3 className="text-lg font-bold text-white">{point.name}</h3>
                        <p className="text-gray-400">{point.address}</p>
                        <p className="text-gray-400">
                        {point.city}, {point.postalCode}
                        </p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Locations;
