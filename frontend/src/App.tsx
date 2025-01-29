import React, { useEffect, useState } from 'react';
import { Disclosure, DisclosureButton, DisclosurePanel, Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react';
import { useNavigate } from 'react-router-dom';

import { useUser } from './CustomerManagement/UserContext';

import MarketPrice from './pages/MarketPrice';
import ElectricityUsage from './pages/ElectricityUsage';
import Expense from './pages/Expense';


function classNames(...classes: string[]) {
return classes.filter(Boolean).join(' ');
}

const App: React.FC = () => {
    const navigate = useNavigate();
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [activeNav, setActiveNav] = useState<string>('Elektri Börs');

    const { user } = useUser();

    const navigation = [
        { name: 'Elektri Börs', current: true },
        { name: 'Tarbimine', current: false },
        { name: 'Kulu', current: false }
    ];
    const userNavigation = [
        { name: 'Logi välja', onclick: () => logoutCustomer() },
    ];

    useEffect(() => {
        if (!user || user.username === 'Guest') {
            navigate('/')
        }
    }, [user, navigate]);

    const logoutCustomer = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/customers/logout/${user?.username}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user?.sessionId),
                credentials: 'include',
            });

            if (response.ok) {
                console.log('Logout successful');
                alert('You have been logged out successfully.');

                localStorage.removeItem('jwtToken');
                sessionStorage.removeItem('jwtToken');

                navigate('/');
            } else {
                console.error('Failed to log out');
                alert('Logout failed. Please try again later.');
            }
        } catch (error) {
            console.error('Error during logout:', error);
            setErrorMessage('An error occurred while logging out. Please try again later.');
        }
    };


    const handleNavClick = (name: string) => {
        setActiveNav(name);
    };

    const renderContent = () => {
        switch (activeNav) {
            case 'Elektri Börs':
                return <MarketPrice />;
            case 'Tarbimine':
                return <ElectricityUsage />;
            case 'Kulu':
                return <Expense />;
            default:
                return <MarketPrice />;
        }
    };

    return (
        <div className="bg-slate-800 h-auto min-h-screen">
        <Disclosure as="nav" className="bg-gray-800">
            <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                <div className="flex h-16 items-center justify-between">
                    <div className="flex items-center">
                        <div>
                            <div className="ml-10 flex items-baseline space-x-4">
                                {navigation.map((item) => (
                                    <button
                                    key={item.name}
                                    onClick={() => handleNavClick(item.name)}
                                    aria-current={item.name === activeNav ? 'page' : undefined}
                                    className={classNames(
                                        item.name === activeNav
                                            ? 'bg-emerald-700 text-white'
                                            : 'text-gray-300 hover:bg-gray-700 hover:text-white',
                                        'rounded-md px-3 py-2 text-sm font-medium'
                                    )}
                                    >
                                        {item.name}
                                    </button>
                                ))}
                            </div>
                        </div>
                    </div>
                    <div>
                        <div className="ml-4 flex items-center">
                            <Menu as="div" className="relative ml-3">
                                <div>
                                    <MenuButton className="relative flex max-w-xs items-center rounded-full bg-gray-800 text-sm focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800 focus:outline-hidden">
                                        <span className="absolute -inset-1.5" />
                                        <div className="mr-3">
                                            <div className="text-base/5 font-medium text-white">{user ? user.first_name : 'Guest'}</div>
                                            <div className="text-sm font-medium text-gray-400">{user ? user.username : 'Guest'}</div>
                                        </div>
                                        <div className="p-1 rounded-full items-center bg-emerald-800">
                                            <svg
                                                xmlns="http://www.w3.org/2000/svg"
                                                viewBox="0 0 24 24"
                                                fill="white"
                                                className="size-6"
                                                >
                                                <path
                                                    fillRule="evenodd"
                                                    d="M7.5 6a4.5 4.5 0 1 1 9 0 4.5 4.5 0 0 1-9 0ZM3.751 20.105a8.25 8.25 0 0 1 16.498 0 .75.75 0 0 1-.437.695A18.683 18.683 0 0 1 12 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 0 1-.437-.695Z"
                                                    clipRule="evenodd"
                                                />
                                            </svg>
                                        </div>
                                    </MenuButton>
                                </div>
                                <MenuItems
                                    transition
                                    className="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 ring-1 shadow-lg ring-black/5 transition focus:outline-hidden data-closed:scale-95 data-closed:transform data-closed:opacity-0 data-enter:duration-100 data-enter:ease-out data-leave:duration-75 data-leave:ease-in"
                                    >
                                        {userNavigation.map((item) => (
                                            <MenuItem key={item.name}>
                                                <a className='block w-full'>
                                                    <button
                                                        onClick={item.onclick}
                                                        className="w-full flex justify-start px-4 py-2 text-sm text-gray-700 data-focus:bg-gray-100 data-focus:outline-hidden  hover:bg-slate-300"
                                                        >
                                                        {item.name}
                                                    </button>
                                                </a>
                                            </MenuItem>
                                        ))}
                                </MenuItems>
                            </Menu>
                        </div>
                    </div>
                </div>
            </div>

            <DisclosurePanel>
                <div className="space-y-1 px-2 pt-2 pb-3 sm:px-3">
                    {navigation.map((item) => (
                        <DisclosureButton
                            key={item.name}
                            as="a"
                            aria-current={item.current ? 'page' : undefined}
                            className={classNames(
                            item.current ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white',
                            'block rounded-md px-3 py-2 text-base font-medium'
                            )}
                        >
                            {item.name}
                        </DisclosureButton>
                    ))}
                </div>
                <div className="border-t border-gray-700 pt-4 pb-3">
                    <div className="flex items-center px-5">
                        <div className="shrink-0">
                            <div className="p-1 rounded-full items-center bg-emerald-800">
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    viewBox="0 0 24 24"
                                    fill="white"
                                    className="size-6" >
                                    <path
                                        fillRule="evenodd"
                                        d="M7.5 6a4.5 4.5 0 1 1 9 0 4.5 4.5 0 0 1-9 0ZM3.751 20.105a8.25 8.25 0 0 1 16.498 0 .75.75 0 0 1-.437.695A18.683 18.683 0 0 1 12 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 0 1-.437-.695Z"
                                        clipRule="evenodd" />
                                </svg>
                            </div>
                        </div>
                        <div className="ml-3">
                            <div className="text-base/5 font-medium text-white">{user ? user.first_name : 'Guest'}</div>
                            <div className="text-sm font-medium text-gray-400">{user ? user.username : 'Guest'}</div>
                        </div>
                    </div>
                    <div className="mt-3 space-y-1 px-2">
                        {userNavigation.map((item) => (
                            <DisclosureButton
                            key={item.name}
                            as="a"
                            onClick={item.onclick}
                            className="block rounded-md px-3 py-2 text-base font-medium text-gray-400 hover:bg-gray-700 hover:text-white"
                            >
                            {item.name}
                            </DisclosureButton>
                        ))}
                    </div>
                </div>
            </DisclosurePanel>
        </Disclosure>
        <header className="bg-emerald-800 shadow-sm">
            <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
                <h1 className="text-3xl font-bold tracking-tight text-white">{activeNav}</h1>
            </div>
        </header>
        <main>
            <div className="text-white mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
                {renderContent()}
            </div>
        </main>
        </div>
    );
};

export default App;
