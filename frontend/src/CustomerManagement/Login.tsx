import React, { useState } from 'react';
import { EyeIcon, EyeSlashIcon } from '@heroicons/react/24/solid';
import { Link, useNavigate } from 'react-router-dom';

import { useUser } from './UserContext'


interface LoginState {
    username: string;
    password: string;
}

const Login: React.FC = () => {
    const [loginState, setLoginState] = useState<LoginState>({ username: '', password: '' });
    const [errorMessage, setErrorMessage] = useState<String | null>(null);
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();
    const { setUser } = useUser();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setLoginState({ ...loginState, [name]: value});
    }

    const togglePasswordVisibility = () => {
        setShowPassword((prev) => !prev);
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setErrorMessage(null);

        try {
            console.log('Sending login request:', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginState),
            });
            const response = await fetch('http://localhost:8080/api/customers/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginState),
                credentials: 'include',
            });

            if (!response.ok) {
                if (response.status === 401) {
                    setErrorMessage('Invalid username or password');
                } else {
                    setErrorMessage('An error occurred while trying to connect to the server. Please try again later.')
                }
                return;
            }
            const data = await response.json();
            setUser(data);
            console.log(data);
            alert("Login successful!");
            navigate('/dashboard');
        } catch (error) {
            console.error("Error during logion:", error);
            setErrorMessage("An error occurred while trying to connect to the server. Please try again later.")
        }
    };

    return (
        <div className="bg-slate-800 h-[100vh] text-white flex justify-center items-center">
            <div className="bg-slate-800 border border-slate-400 rounded-md p-8 ">
                <div>
                    <h1 className='text-4xl font-bold text-center mb-6'>Enefit Power App</h1>

                    <div className='mt-10 sm:mx-auto sm:w-full sm:max-w-sm'>
                        <form className="space-y-6" onSubmit={handleSubmit}>
                            <div>
                                <label htmlFor='username' className='block text-sm/6 font-medium'>Kasutajanimi</label>
                                <div className='mt-2'>
                                    <input
                                        type='text'
                                        name='username'
                                        id='username'
                                        value={loginState.username}
                                        onChange={handleChange}
                                        autoComplete='username'
                                        required
                                        className='block w-full rounded-md bg-slate-700 px-3 py-1.5 text-base focus:outline-none focus:border-0  focus:border-b-2 focus:border-emerald-400'
                                    />
                                </div>
                            </div>

                            <div>
                                <div className='flex items-center justify-between'>
                                    <label htmlFor='password' className='block text-sm/6 font-medium'>Parool</label>
                                    <div className='text-sm'>
                                        <a href="#" className='font-semibold text-white hover:text-emerald-400'>Unustasid Parooli?</a>
                                    </div>
                                </div>
                                <div>
                                    <div className='relative'>
                                        <input
                                            type={showPassword ? 'text' : 'password'}
                                            name='password'
                                            id='password'
                                            value={loginState.password}
                                            onChange={handleChange}
                                            required
                                            className='block w-full rounded-md bg-slate-700 px-3 py-1.5 text-base focus:outline-none focus:border-0  focus:border-b-2 focus:border-emerald-400'
                                        />
                                        <button type='button' onClick={togglePasswordVisibility} className='absolute inset-y-0 right-3 flex items-center text-sm text-emerald-400 hover:text-white focus:outline-none'>
                                            {showPassword ? (
                                            <EyeIcon className="h-5 w-5" aria-hidden="true" />
                                            ) : (
                                            <EyeSlashIcon className="h-5 w-5" aria-hidden="true" />
                                            )}
                                        </button>
                                    </div>
                                </div>
                                {errorMessage && <p className='text-red-500 text-sm'>{errorMessage}</p>}
                            </div>

                            <div>
                                <button type='submit' className='flex w-full justify-center rounded-md bg-emerald-800 px-3 py-1.5 text-sm/6 font-semibold text-white hover:bg-emerald-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-red-800'>LOGI SISSE</button>
                            </div>

                            <div className='mt-4'>
                                <Link to="/register">
                                    <button type="button" className="flex w-full justify-center rounded-md bg-green-900 py-0.5 text-sm/6 font-semibold text-white hover:bg-green-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-red-800">
                                    Registreeri UUS kasutaja
                                    </button>
                                </Link>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
    </div>
    );
};

export default Login;
