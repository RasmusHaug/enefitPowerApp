import React, { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import dayjs from 'dayjs';

import { useUser } from '../CustomerManagement/UserContext';

interface MeteringPoint {
    meteringPointId: number;
    name: string;
    address: string;
    city: string;
    postalCode: string;
}

interface Consumption {
    consumptionTime: string;
    amount: number;
    amountUnit: string;
    consumptionId: string;
}

const Locations: React.FC = () => {
    const [meteringPoints, setMeteringPoints] = useState<MeteringPoint[]>([]);
    const [consumptions, setConsumptions] = useState<{ [key: string]: Consumption[] }>({});
    const [isAddingLocation, setIsAddingLocation] = useState(false);
    const [newMeteringPoint, setNewMeteringPoint] = useState({
            name: '',
            address: '',
            city: '',
            postalCode: '',
    });
    const [isAddingConsumption, setIsAddingConsumption] = useState(false);
    const [newConsumptionData, setNewConsumptionData] = useState ({
            meteringPoint: '',
            amount: '',
            amountUnit: '',
            consumptionTime: '',
    });
    const { user } = useUser();
    const customerId = user?.customerId;
    const token = user?.sessionId;

    const fetchMeteringPoints = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/customers/${customerId}/get-metering-points`, {
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
            fetchConsumptions(data);
        } catch (error) {
            console.error('Error fetching metering points:', error);
        }
    };

    const fetchConsumptions = async (meteringPoints: MeteringPoint[]) => {
        try {
            const consumptionPromises = meteringPoints.map((point) => {

                return fetch(
                    `http://localhost:8080/api/customers/${customerId}/${point.meteringPointId}/get-consumptions`,
                    {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            Authorization: `Bearer ${token}`,
                        },
                        credentials: 'include',
                    }
                )
                .then((response) => response.json())
                .then((data) => {
                    setConsumptions((prev) => ({
                        ...prev,
                        [point.meteringPointId]: data,
                    }));
                })
                .catch((error) => {
                    console.error('Error fetching consumption:', error);
                });
            });

            await Promise.all(consumptionPromises);
        } catch (error) {
            console.error('Error in fetching consumptions:', error);
        }
    };

    const addMeteringPoint = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await fetch( `http://localhost:8080/api/customers/${customerId}/add-metering-point`, {
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
            setIsAddingLocation(false);
        } catch (error) {
            console.error('Error adding metering point:', error);
        }
    };

    const addNewConsumption = async (e: React.FormEvent) => {
        e.preventDefault();

        const { meteringPoint, amount, consumptionTime } = newConsumptionData;
        const newAmount = parseFloat(amount);
        const consumptionDate = dayjs(consumptionTime).format('YYYY-MM-DD');

        try {
            const consumptionData = {
                meteringPointId: parseInt(meteringPoint, 10),
                amount: newAmount,
                amountUnit: 'kWh',
                consumptionTime: consumptionDate,
            };

            const response = await fetch(`http://localhost:8080/api/customers/${customerId}/${meteringPoint}/add-consumption`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                credentials: 'include',
                body: JSON.stringify(consumptionData),
            });

            if (!response.ok) {
                console.error(response.body)
                throw new Error('Failed to add new consumption.');
            }

            const addedConsumption = await response.json();
            setConsumptions((prev) => {
                const existingConsumptions = prev[meteringPoint] || [];
                const consumptionDate = dayjs(addedConsumption.consumptionTime).format('YYYY-MM-DD');

                // Check if a record for the same date already exists
                const updatedConsumptions = existingConsumptions.map((record) =>
                    dayjs(record.consumptionTime).format('YYYY-MM-DD') === consumptionDate
                        ? { ...record, amount: record.amount + addedConsumption.amount } // Sum amounts
                        : record
                );

                // If no existing record, add a new one
                if (!updatedConsumptions.some(record => dayjs(record.consumptionTime).format('YYYY-MM-DD') === consumptionDate)) {
                    updatedConsumptions.push(addedConsumption);
                }

                return { ...prev, [meteringPoint]: updatedConsumptions };
            });

            setNewConsumptionData({
                meteringPoint: '',
                amount: '',
                amountUnit: '',
                consumptionTime: '',
            });
            setIsAddingConsumption(false);
        } catch (error) {
            console.error('Error adding consumption:', error);
        }
    };

    const handleNewMeteringPointInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setNewMeteringPoint((prev) => ({
        ...prev,
        [name]: value,
        }));
    };

    const handleAddNewConsumption = (meteringPointId: number) => {
        setNewConsumptionData((prev) => ({
            ...prev,
            meteringPoint: meteringPointId.toString(),
        }));
        setIsAddingConsumption(true);
    }

    const handleAddNewLocation = () => {
        setIsAddingLocation(true);
    };

    const handleConsumptionInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setNewConsumptionData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    useEffect(() => {
        fetchMeteringPoints();
    }, []);

    return (
        <div className="p-3 bg-slate-900 rounded-md">
                        <div className="m-2 p-2 flex justify-between items-center">
                <h1 className="text-2xl font-bold text-white">Salvestatud eluasemed</h1>
                <button
                onClick={handleAddNewLocation}
                className="px-2 py-1 rounded-md bg-green-700 text-white hover:bg-green-500"
                >
                Lisa Uus
                </button>
            </div>

            {/* Add Metering Point/Location Form */}
            {isAddingLocation && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                    <div className="bg-slate-700 rounded-md p-6 w-96 border-4 border-emerald-700">
                        <h2 className="text-lg font-bold mb-4">Lisa uus eluase</h2>
                        <form onSubmit={addMeteringPoint}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium">Eluase Nimetus</label>
                                <input
                                    type="text"
                                    name="name"
                                    value={newMeteringPoint.name}
                                    onChange={handleNewMeteringPointInputChange}
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
                                    onChange={handleNewMeteringPointInputChange}
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
                                    onChange={handleNewMeteringPointInputChange}
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
                                    onChange={handleNewMeteringPointInputChange}
                                    className="w-full p-2 border rounded-md text-black"
                                    required
                                />
                            </div>
                            <div className="flex justify-end">
                                <button
                                    type="button"
                                    onClick={() => setIsAddingLocation(false)}
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

            {/* Add Consumption Form */}
            {isAddingConsumption && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                    <div className="bg-slate-700 rounded-md p-6 w-96 border-4 border-emerald-700">
                        <h2 className="text-lg font-bold mb-4">Lisa uus tarbimine</h2>
                        <form onSubmit={addNewConsumption}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium">Kogus kWh</label>
                                <input
                                    type="number"
                                    name="amount"
                                    value={newConsumptionData.amount}
                                    onChange={handleConsumptionInputChange}
                                    className="w-full p-2 border rounded-md text-black"
                                    required
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium">Kuupäev kulutatud</label>
                                <input
                                    type="date"
                                    name="consumptionTime"
                                    value={newConsumptionData.consumptionTime}
                                    onChange={handleConsumptionInputChange}
                                    className="w-full p-2 border rounded-md text-black"
                                    required
                                />
                            </div>
                            <div className="flex justify-end">
                                <button
                                    type="button"
                                    onClick={() => setIsAddingConsumption(false)}
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

            <div className="flex flex-col gap-4">
                {meteringPoints.map((point) => (
                    <div key={point.meteringPointId} className="p-4 bg-slate-800 rounded-md flex justify-between items-center">
                        <div className="flex-1 min-w-[100px] max-w-[300px]">
                            <h2 className="text-lg font-bold text-white">{point.name}</h2>
                            <p className="text-gray-400">{point.address}</p>
                            <p className="text-gray-400">
                                {point.city}, {point.postalCode}
                            </p>
                        </div>
                        <div className="flex-1 relative">
                            {consumptions[point.meteringPointId] && consumptions[point.meteringPointId].length > 0 ? (
                                <ResponsiveContainer width="100%" height={200}>
                                    <BarChart data={consumptions[point.meteringPointId] } margin={{ top: 5, right: 30, left: 20, bottom: 5, }} >
                                        <CartesianGrid strokeDasharray="3 3" />
                                        <XAxis dataKey="consumptionTime" tick={{ fill: 'white' }} reversed={true} />
                                        <YAxis tick={{ fill: 'white' }} />
                                        <Tooltip
                                            contentStyle={{ backgroundColor: '#0f172a', border: '2px solid #334155' }}
                                            formatter={(value) => [`${value} kWh`, 'kulu']} />
                                        <Bar dataKey="amount" fill="#047857" radius={[5, 5, 0, 0]} />
                                    </BarChart>
                                </ResponsiveContainer>
                            ) : (
                                <p className="text-gray-400">Ei ole andmeid.</p>
                            )}
                        </div>
                        <div>
                            <button
                                onClick={() => handleAddNewConsumption(point.meteringPointId)}
                                className="px-2 py-1 bg-green-700 text-white rounded-md hover:bg-green-500"
                            >
                                Lisa uus tarbimine
                            </button>
                        </div>
                    </div>
                ))}
            </div>

        </div>
    );
};

export default Locations;