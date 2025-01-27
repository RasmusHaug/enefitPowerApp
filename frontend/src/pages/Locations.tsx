import React from 'react';

const Calendar: React.FC = () => {
    return (
        <div className="p-5 bg-slate-900 rounded-md">
            <div className='flex justify-between'>
                <h1 className="text-2xl font-bold text-white mb-4">Sinu Eluasemed</h1>
                <div>
                    <button className='p-2 rounded-md bg-green-700'>
                        Lisa uus
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Calendar;