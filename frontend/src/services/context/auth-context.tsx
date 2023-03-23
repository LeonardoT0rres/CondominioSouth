import React, { createContext, useCallback, useEffect, useState } from 'react';

let logoutTimer: any;

type authContextType = {
  token: string;
  userIsLoggedIn: boolean;
  login: (token: string, sessionTime: string) => void;
  logout: () => void;
};

const authContextValores: authContextType = {
  token: '',
  userIsLoggedIn: false,
  login: (token, sessionTime) => {},
  logout: () => {},
};

const AuthContext = createContext<authContextType>(authContextValores);
// ------------------------------------------------
// Funções acessórias para o Componente Provider:

// Calcula o tempo restante to token:
const calculateTimeLeft = (sessionTime: string) => {
  const currentTime = new Date().getTime();
  const expirationTime = new Date(sessionTime).getTime();

  const timeLeft = expirationTime - currentTime;

  return timeLeft;
};

// Busca o objeto com o token armazenado e seu tempo de duração:

const fetchStoredToken:
  | {
      token: string | null;
      duration: number;
    }
  | any = () => {
  const storedToken = localStorage.getItem('token');
  const storedSessionTime = localStorage.getItem('sessionTime');

  const timeLeft = calculateTimeLeft(storedSessionTime!);

  if (timeLeft <= 60000) {
    // se faltar só 1min...
    localStorage.removeItem('token');
    localStorage.removeItem('sessionTime');
    return null;
  } // Encerra a sessão

  return {
    token: storedToken,
    duration: timeLeft,
  };
};

// ------------------------------------------------
type AuthContextProviderProps = {
  children: JSX.Element;
};

export const AuthContextProvider: React.FC<AuthContextProviderProps> = ({
  children,
}) => {
  const tokenData = fetchStoredToken();
  let startToken;
  if (tokenData) {
    // se houver dados
    startToken = tokenData.token;
  }

  const [token, setToken] = useState<string>(startToken);

  const userIsLoggedIn = !!token;
  // !! é 'not not': convertendo em booleano true ou false (string com conteúdo ou vazia);

  const logoutHandler = useCallback(() => {
    setToken('');
    localStorage.removeItem('token');
    localStorage.removeItem('sessionTime');

    if (logoutTimer) {
      // Se há timer do logoutHandler (no useEffect abaixo)
      clearTimeout(logoutTimer); // Então limpamos o timeout;
    }
  }, []); // callback para armazenar o método na memória e evitar possíveis loops lá no useEffect do final (ainda não entendi essa conceito 100%, mas por instrução dos cursos vou manter a prática);

  const loginHandler = (
    token: string,
    sessionTime: string,
  ) => {

    setToken(token);
    localStorage.setItem('token', token);
    localStorage.setItem('sessionTime', sessionTime); // string para usarmos em Date() da função calcular.

    const tempoRestante = calculateTimeLeft(sessionTime);
    // Nosso callback acima é chamado aqui (tempo em ms):
    logoutTimer = setTimeout(logoutHandler, tempoRestante);
  };

  // useEffect hook para mudanças dos dados do Token (exceto pela inicialização nula do início):
  useEffect(() => {
    if (tokenData) {
      // console.log(tokenData.duration); // Reduz a cada F5 para testar no console;
      logoutTimer = setTimeout(logoutHandler, tokenData.duration);
    }
  }, [tokenData, logoutHandler]);

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
