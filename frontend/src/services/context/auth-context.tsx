import React, { createContext, useCallback, useEffect, useState } from 'react';

type authContextType = {
  token: string | null;
  userIsLoggedIn: boolean;
  login: (token: string) => void;
  logout: () => void;
};

const authContextValores: authContextType = {
  token: '',
  userIsLoggedIn: false,
  login: (token) => {},
  logout: () => {},
};

const AuthContext = createContext<authContextType>(authContextValores);
// ------------------------------------------------
// Funções acessórias para o Componente Provider:

// Busca o objeto com o token armazenado e seu tempo de duração:

// ------------------------------------------------
type AuthContextProviderProps = {
  children: JSX.Element;
};

export const AuthContextProvider: React.FC<AuthContextProviderProps> = ({
  children,
}) => {
  const [token, setToken] = useState<string | null>('');

  useEffect(() => {
    setToken(localStorage.getItem('token'));
  }, []);
  const userIsLoggedIn = !!token;

  const logoutHandler = useCallback(() => {
    setToken('');
    localStorage.removeItem('token');
  }, []); // callback para armazenar o método na memória e evitar possíveis loops lá no useEffect do final (ainda não entendi essa conceito 100%, mas por instrução dos cursos vou manter a prática);

  const loginHandler = (token: string) => {
    localStorage.setItem('token', token);
    setToken(token);
  };

  const contextValue: authContextType = {
    token: token,
    userIsLoggedIn: userIsLoggedIn,
    login: loginHandler,
    logout: logoutHandler,
  };

  return (
    <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
  );
};

export default AuthContext;
