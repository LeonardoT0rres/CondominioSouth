import { BrowserRouter } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import { Router } from './Router';
import { AuthContextProvider } from './services/context/auth-context';

// Provider de Contexto pode envolver o Router quando for feito.

function App() {
  return (
    <AuthContextProvider>
      <BrowserRouter>
        <Router />
        <ToastContainer />
      </BrowserRouter>
    </AuthContextProvider>
  )
}

export default App
