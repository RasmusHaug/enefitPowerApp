import React, { useState } from 'react';
import { EyeIcon, EyeSlashIcon } from '@heroicons/react/24/solid';
import { Link, useNavigate } from 'react-router-dom';

interface RegisterState {
    firstName: string;
    lastName: string;
    username: string;
    password: string;
}

const Register: React.FC = () => {
    const [registerState, setRegisterState] = useState<RegisterState>({
        firstName: '',
        lastName: '',
        username: '',
        password: '',
    });
    const [showPassword, setShowPassword] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setRegisterState({ ...registerState, [name]: value });
    };

    const togglePasswordVisibility = () => {
        setShowPassword((prev) => !prev);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setErrorMessage(null);

        try {
            const response = await fetch('http://localhost:8080/api/customers/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(registerState),
            });

            if (!response.ok) {
                if (response.status === 400) {
                    setErrorMessage('Invalid data. Please check your inputs.');
                } else {
                    setErrorMessage('An error occurred while trying to register. Please try again later.');
                }
                return;
            }
            // On successfull registration for user experience log them in as well.
            const loginResponse = await fetch('http://localhost:8080/api/customers/login', {
                method: 'POST',
                headers: {
                'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                username: registerState.username,
                password: registerState.password,
                }),
            });
            if (!loginResponse.ok) {
                setErrorMessage('Login failed after registration.');
                return;
            }
            navigate('/dashboard');

        } catch (error) {
            console.error("Error during registration:", error);
            setErrorMessage("An error occurred while trying to connect to the server. Please try again later.");
        }
    };

    return (
        <div className='bg-slate-800 h-[100vh] text-white flex justify-center items-center'>
            <div className="bg-slate-800 border border-slate-400 rounded-md p-8">
                <h1 className="text-4xl font-bold text-center mb-6 text-white">Registreeri kasutaja</h1>
                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form className="space-y-6" onSubmit={handleSubmit}>

                        <div>
                            <label htmlFor="firstName" className="block text-sm font-medium text-white">
                                Eesnimi
                            </label>
                            <div className="mt-2">
                                <input
                                    type="text"
                                    name="firstName"
                                    id="firstName"
                                    value={registerState.firstName}
                                    onChange={handleChange}
                                    required
                                    className="block w-full rounded-md bg-slate-700 px-3 py-1.5 text-base text-white focus:outline-none focus:border-0 focus:border-b-2 focus:border-emerald-400"
                                />
                            </div>
                        </div>

                        <div>
                            <label htmlFor="lastName" className="block text-sm font-medium text-white">
                                Perekonnanimi
                            </label>
                            <div className="mt-2">
                                <input
                                    type="text"
                                    name="lastName"
                                    id="lastName"
                                    value={registerState.lastName}
                                    onChange={handleChange}
                                    required
                                    className="block w-full rounded-md bg-slate-700 px-3 py-1.5 text-base text-white focus:outline-none focus:border-0 focus:border-b-2 focus:border-emerald-400"
                                />
                            </div>
                        </div>

                        <div>
                            <label htmlFor="username" className="block text-sm font-medium text-white">
                                Emaili aadress
                            </label>
                            <div className="mt-2">
                                <input
                                    type="text"
                                    name="username"
                                    id="username"
                                    value={registerState.username}
                                    onChange={handleChange}
                                    required
                                    className="block w-full rounded-md bg-slate-700 px-3 py-1.5 text-base text-white focus:outline-none focus:border-0 focus:border-b-2 focus:border-emerald-400"
                                />
                            </div>
                        </div>

                        <div>
                            <label htmlFor="password" className="block text-sm font-medium text-white">
                                Parool
                            </label>
                            <div className="relative mt-2">
                                <input
                                    type={showPassword ? 'text' : 'password'}
                                    name="password"
                                    id="password"
                                    value={registerState.password}
                                    onChange={handleChange}
                                    required
                                    className="block w-full rounded-md bg-slate-700 px-3 py-1.5 text-base text-white focus:outline-none focus:border-0 focus:border-b-2 focus:border-emerald-400"
                                />
                                <button
                                    type="button"
                                    onClick={togglePasswordVisibility}
                                    className="absolute inset-y-0 right-3 flex items-center text-sm text-emerald-400 hover:text-white focus:outline-none"
                                >
                                    {showPassword ? (
                                        <EyeIcon className="h-5 w-5" aria-hidden="true" />
                                    ) : (
                                        <EyeSlashIcon className="h-5 w-5" aria-hidden="true" />
                                    )}
                                </button>
                            </div>
                        </div>

                        {errorMessage && <p className="text-red-500 text-sm">{errorMessage}</p>}

                        <div>
                            <button
                                type="submit"
                                className="flex w-full justify-center rounded-md bg-emerald-800 px-3 py-1.5 text-sm font-semibold text-white hover:bg-emerald-500 focus:outline-none focus:ring-2 focus:ring-emerald-400"
                                >
                                REGISTREERI KASUTAJA
                            </button>
                        </div>
                        <div className="mt-6 text-center">
                            <span className="block text-sm text-gray-400 mb-2">Omad juba kasutajat?</span>
                            <Link to="/" className="inline-block text-sm font-semibold text-emerald-400 hover:text-emerald-600 transition-colors duration-200">
                                <button 
                                    type="button"
                                    className="bg-transparent text-emerald-400 hover:bg-emerald-800 hover:text-white rounded-md px-4 py-1 focus:outline-none focus:ring-2 focus:ring-emerald-400 "
                                    >
                                    Tagasi sisse logima
                                </button>
                            </Link>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Register;
