    import React, { useState } from 'react';
    import { Disclosure, DisclosureButton, DisclosurePanel, Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react';
    import { Bars3Icon, BellIcon, XMarkIcon } from '@heroicons/react/24/outline';
    import { useNavigate } from 'react-router-dom';


    function classNames(...classes: string[]) {
    return classes.filter(Boolean).join(' ');
    }

    const App: React.FC = () => {
    const navigate = useNavigate();
    const [errorMessage, setErrorMessage] = useState<string | null>(null);


    const user = {
        first_name: 'John',
        username: 'johndoe',
    };
    const navigation = [
        { name: 'Ülevaade', current: true },
        { name: 'Kalender', current: false },
        { name: 'Aruanded', current: false },
    ];
    const userNavigation = [
        { name: 'Sinu Konto' },
        { name: 'Seaded' },
        { name: 'Logi välja', onclick: () => logoutCustomer() },
    ];

    const logoutCustomer = async () => {
        try {
        const response = await fetch('http://localhost:8080/api/customers/logout', {
            method: 'POST',
            credentials: 'include',
        });

        if (response.ok) {
            console.log('Logout successful');
            alert('You have been logged out successfully.');
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

    return (
        <div className="bg-slate-800 h-[100vh]">
        <Disclosure as="nav" className="bg-gray-800">
            <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            <div className="flex h-16 items-center justify-between">
                <div className="flex items-center">
                <div className="hidden md:block">
                    <div className="ml-10 flex items-baseline space-x-4">
                    {navigation.map((item) => (
                        <a
                        key={item.name}
                        aria-current={item.current ? 'page' : undefined}
                        className={classNames(
                            item.current ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white',
                            'rounded-md px-3 py-2 text-sm font-medium'
                        )}
                        >
                        {item.name}
                        </a>
                    ))}
                    </div>
                </div>
                </div>
                <div className="hidden md:block">
                <div className="ml-4 flex items-center md:ml-6">
                    {/* Profile dropdown */}
                    <Menu as="div" className="relative ml-3">
                    <div>
                        <MenuButton className="relative flex max-w-xs items-center rounded-full bg-gray-800 text-sm focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800 focus:outline-hidden">
                        <span className="absolute -inset-1.5" />
                        <span className="sr-only">Ava kasutaja menüü</span>
                        {/* USER ICON */}
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
                            <a
                            className="block px-4 py-2 text-sm text-gray-700 data-focus:bg-gray-100 data-focus:outline-hidden"
                            onClick={item.onclick}
                            >
                            {item.name}
                            </a>
                        </MenuItem>
                        ))}
                    </MenuItems>
                    </Menu>
                </div>
                </div>
                <div className="-mr-2 flex md:hidden">
                {/* Mobile menu button */}
                <DisclosureButton className="group relative inline-flex items-center justify-center rounded-md bg-gray-800 p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800 focus:outline-hidden">
                    <span className="absolute -inset-0.5" />
                    <span className="sr-only">Ava peamenüü</span>
                    <Bars3Icon aria-hidden="true" className="block size-6 group-data-open:hidden" />
                    <XMarkIcon aria-hidden="true" className="hidden size-6 group-data-open:block" />
                </DisclosureButton>
                </div>
            </div>
            </div>

            <DisclosurePanel className="md:hidden">
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
                    {/* USER ICON */}
                </div>
                <div className="ml-3">
                    <div className="text-base/5 font-medium text-white">{user.first_name}</div>
                    <div className="text-sm font-medium text-gray-400">{user.username}</div>
                </div>
                <button
                    type="button"
                    className="relative ml-auto shrink-0 rounded-full bg-gray-800 p-1 text-gray-400 hover:text-white focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800 focus:outline-hidden"
                >
                    <span className="absolute -inset-1.5" />
                    <span className="sr-only">View notifications</span>
                    <BellIcon aria-hidden="true" className="size-6" />
                </button>
                </div>
                <div className="mt-3 space-y-1 px-2">
                {userNavigation.map((item) => (
                    <DisclosureButton
                    key={item.name}
                    as="a"
                    onClick={item.onclick} // Attach onClick here to trigger logout
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
            <h1 className="text-3xl font-bold tracking-tight text-white">Ülevaade</h1>
            </div>
        </header>
        <main>
            <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">{/* Your content */}</div>
        </main>
        </div>
    );
    };

    export default App;
