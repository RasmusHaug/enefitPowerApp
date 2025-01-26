import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './App.tsx'
import Login from './CustomerManagement/Login.tsx'
import Register from './CustomerManagement/Register.tsx'
import { UserProvider } from './CustomerManagement/UserContext.tsx'

const router = createBrowserRouter([
{
    path: '/',
    element: <Login />,
},
{
    path: '/register',
    element: <Register />,
},
{
    path: '/dashboard',
    element: <App />,
},
])

createRoot(document.getElementById('root')!).render(
<StrictMode>
    <UserProvider>
        <RouterProvider router={router} />
    </UserProvider>
</StrictMode>,
)
