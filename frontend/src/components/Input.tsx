import React from 'react';

interface InputProps {
  handleChange: (e: React.ChangeEvent<HTMLInputElement>) => void; // Function to handle input changes
  value: string; // The current value of the input field
  labelText: string; // The label text for the input
  id: string; // The unique identifier for the input field
  type?: string; // The type of input (default is "text")
  placeholder?: string; // Placeholder text for the input field
}

const fixedInputClass =
    'rounded-md appearance-none block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-purple-500 focus:border-purple-500';

const Input: React.FC<InputProps> = ({
    handleChange,
    value,
    labelText,
    id,
    type = 'text',
    placeholder = '',
}) => {
    return (
    <div className="my-5">
        <label htmlFor={id} className="block text-sm font-medium text-gray-700 mb-1">
        {labelText}
        </label>
        <input
        onChange={handleChange}
        value={value}
        id={id}
        type={type}
        className={fixedInputClass}
        placeholder={placeholder}
        required
        />
    </div>
    );
};

export default Input;
