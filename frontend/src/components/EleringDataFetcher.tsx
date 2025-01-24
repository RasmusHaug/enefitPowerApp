import { useEffect, useState } from "react";


interface EleringDataModel {
    centsPerKwh: number;
    centsPerKwhWithVat: number;
    eurPerMwh: number;
    eurPerMwhWithVat: number;
    fromDateTime: string;
    toDateTime: string;
}

const EleringDataFetcher = () => {
    const [data, setData] = useState<EleringDataModel[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchData = async () => {
        setLoading(true);
        try {
            const startDateTime = '2025-01-22T00:00:00Z';
            const endDateTime = '2025-01-22T23:59:00Z';
            const response = await fetch(`http://localhost:8080/api/fetch-elering-data?startDateTime=${startDateTime}&endDateTime=${endDateTime}`);

            if (!response.ok) {
            throw new Error(`${response.status} ${response.statusText}`);
            }

            const jsonData: EleringDataModel[] = await response.json();
            setData(jsonData);
        } catch (error) {
            if (error instanceof Error) {
            console.error('Error fetching data:', error.message);
            setError(error.message);
            } else {
            console.error('Unknown error occurred');
            setError('An unknown error occurred');
            }
        } finally {
            setLoading(false);
        }
        };

        fetchData();
    }, []);

    return (
        <>
        {loading ? (
            <h2>Loading...</h2>
        ) : error ? (
            <h2>Error: {error}</h2>
        ) : (
            <ul>
            {data.map((item, index) => (
                <li key={index}>
                Cents Per Kwh: {item.centsPerKwh}, From: {item.fromDateTime}, To: {item.toDateTime}
                </li>
            ))}
            </ul>
        )}
        </>
    );
}


export default EleringDataFetcher;