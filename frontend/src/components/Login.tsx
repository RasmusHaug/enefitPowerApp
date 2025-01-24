import React, { useState } from 'react';
import Input from './Input';

interface LoginState {
    email: string;
    password: string;
}

const Login: React.FC = () => {
    const [loginState, setLoginState] = useState<LoginState>({ email: '', password: '' });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setLoginState({ ...loginState, [e.target.id]: e.target.value });
    };

    const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Login data:', loginState);
    // Add API call here to authenticate user
    };

    return (
    <form onSubmit={handleSubmit} className="mt-8 space-y-6">
        <Input
            handleChange={handleChange}
            value={loginState.email}
            labelText="Email"
            id="email"
            type="email"
            placeholder="Enter your email" />
        <Input
            handleChange={handleChange}
            value={loginState.password}
            labelText="Password"
            id="password"
            type="password"
            placeholder="Enter your password" />
        <button
            type="submit"
            className="w-full bg-purple-600 text-white py-2 rounded-md hover:bg-purple-700 transition duration-200" >
            Login
        </button>
    </form>
    );
};

export default Login;
