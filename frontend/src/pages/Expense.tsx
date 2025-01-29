import React, { useEffect, useState } from 'react';
import { useUser } from '../CustomerManagement/UserContext';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

interface MarketData {
    date: string;
    centsPerKwhWithVat: number;
}

interface Consumption {
    date: string;
    amount: number;
}

interface ExpenseResult {
    date: string;
    totalExpense: number;
}

const Expense: React.FC = () => {
    const [marketData, setMarketData] = useState<MarketData[]>([]);
    const [consumptions, setConsumptions] = useState<Consumption[]>([]);
    const [expenseResults, setExpenseResults] = useState<ExpenseResult[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const { user } = useUser();
    const customerId = user?.customerId;
    const token = user?.sessionId;

    useEffect(() => {
        const fetchExpenses = async () => {
            try {
                setLoading(true);
                setError(null);

                const eleringResponse = await fetch(`http://localhost:8080/api/fetch-elering-date-year`);
                const consumptionResponse = await fetch(`http://localhost:8080/api/customers/${customerId}/get-consumptions`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                    credentials: 'include',
                });

                if (!eleringResponse.ok) {
                    throw new Error('Failed to fetch Elering data.');
                }
                if (!consumptionResponse.ok) {
                    throw new Error('Failed to fetch Consumption data.');
                }

                const eleringResult = await eleringResponse.json();
                const consumptionResult = await consumptionResponse.json();

                const transformedEleringData = eleringResult.map((entry: any) => ({
                    date: entry.date,
                    centsPerKwhWithVat: entry.centsPerKwhWithVat,
                }));

                const transformedConsumptionData = consumptionResult.map((entry: any) => ({
                    date: entry.date,
                    amount: entry.amount,
                }));

                setMarketData(transformedEleringData);
                setConsumptions(transformedConsumptionData);
            } catch (error: any) {
                setError(error.message || 'An unexpected error occurred.');
            } finally {
                setLoading(false);
            }
        };

        if (customerId && token) {
            fetchExpenses();
        }
    }, [customerId, token]);

    useEffect(() => {
        const calculateExpenses = () => {
            const results: ExpenseResult[] = [];

            consumptions.forEach((consumption) => {
                const consumptionMonth = consumption.date.slice(0, 7);
                const matchingMarketData = marketData.find(
                    (market) => market.date.slice(0, 7) === consumptionMonth
                );

                if (matchingMarketData) {
                    const totalExpense = matchingMarketData.centsPerKwhWithVat * consumption.amount;
                    results.push({
                        date: consumption.date,
                        totalExpense: totalExpense / 100,
                    });
                }
            });

            setExpenseResults(results);
        };

        if (marketData.length > 0 && consumptions.length > 0) {
            calculateExpenses();
        }
    }, [marketData, consumptions]);

    const chartData = expenseResults.map((expense) => ({
        date: expense.date,
        centsPerKwh: expense.totalExpense,
    }));

    return (
        <div className='p-5 bg-slate-900 rounded-md'>
            <div className=''>
                <h1 className='text-2xl font-bold text-white mb-4'>Kulu Kokku</h1>
            </div>
            <div>
                {loading && <p>Loading...</p>}
                {error && <p className="text-red-500">{error}</p>}
                {!loading && !error && chartData.length > 0 && (
                    <div className="p-5 bg-slate-700 rounded-3xl">
                        <ResponsiveContainer width="100%" height={400}>
                            <BarChart data={chartData} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="date" tick={{ fill: 'white' }} />
                                <YAxis tick={{ fill: 'white' }} />
                                <Tooltip
                                    contentStyle={{
                                        backgroundColor: '#0f172a',
                                        border: '2px solid #334155',
                                    }}
                                    formatter={(value: number) => [`${value.toFixed(2)} Eurot`, 'Maksumus']}
                                />
                                <Bar dataKey="centsPerKwh" fill="#047857" />
                            </BarChart>
                        </ResponsiveContainer>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Expense;
