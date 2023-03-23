import { ChangeEvent, FormEvent, Fragment, useContext, useState } from 'react';
import { ArrowLeft } from 'phosphor-react';

import { Button } from '../UI/Button/Button';
import { Input } from '../UI/Input/Input';
import { Link, useNavigate } from 'react-router-dom';

import { AuthValues } from '../../types';
import { loginPost, registerPost } from '../../services/AuthService';

import styles from './AuthForm.module.css';
import { toast } from 'react-toastify';

import AuthContext from '../../services/context/auth-context';

export function AuthForm() {
  const [authValues, setAuthValues] = useState<AuthValues>({
    email: '',
    password: '',
  });

  const [isLogin, setIsLogin] = useState<boolean>(true);

  const authCtx = useContext(AuthContext);
  const navigate = useNavigate();

  function changeFunctionHandler() {
    setIsLogin((isLogin) => !isLogin);
  }

  function typeEmailHandler(event: ChangeEvent<HTMLInputElement>) {
    setAuthValues({ ...authValues, email: event.target.value });
  }

  function typePasswordHandler(event: ChangeEvent<HTMLInputElement>) {
    setAuthValues({ ...authValues, password: event.target.value });
  }

  async function loginHandler(event: FormEvent) {
    event.preventDefault();

    // Provisório até ver como vai ser a validação pelo backend em vez de com o sistema do firebase, que já tem uma validação mínima!
    const authDataIsValid = true;

    if (authDataIsValid) {
      if (isLogin) {
        try {
          const response = await loginPost(authValues);

          const data = response.data;
          // [TEMPORÁRIO] Lógica login/token do auth-context COM Firebase (com axios não deu pra pegar o expiresIn, portanto settei 60kk milisegundos = 60k segundos = 1k minutos, se calculei certo... deve dar mais da metade de um dia):
          const expirationTime: Date = new Date(
            new Date().getTime() + 60000 * 1000
          );

          console.log(expirationTime);
          // Agora fazendo o contexto funcionar:
          authCtx.login(data.idToken, expirationTime.toISOString());
          // --------------------------------

          toast.success('Login Realizado!', {
            position: toast.POSITION.TOP_CENTER,
            toastId: 'loginSuccessMessage',
          });

          navigate('/assembleia-lista');
        } catch (error: any) {
          toast.error(
            `Que pena, não foi possível realizar o login: ${error.message}`,
            {
              position: toast.POSITION.TOP_CENTER,
              toastId: 'loginErrorMessage',
            }
          );
        }
      } else {
        try {
          const response = await registerPost(authValues);
          toast.success(
            'E-mail Cadastrado! Usuário pode fazer login agora.',
            {
              position: toast.POSITION.TOP_CENTER,
              toastId: 'registerSuccessMessage',
            }
          );

        } catch (error: any) {
          toast.error(
            `Que pena, não foi possível realizar o cadastro do e-mail`,
            {
              position: toast.POSITION.TOP_CENTER,
              toastId: 'registerErrorMessage',
            }
          );
        }
      }
    } else {
      toast.warn('Dados inválidos. Por favor, verifique as entradas', {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'authWarningMessage',
      });
    }

    setAuthValues({
      email: '',
      password: '',
    });
  }

  const registerFormIsFilled = authValues.email;

  const loginFormIsFilled =
    registerFormIsFilled && authValues.password ? true : false;

  return (
    <div
      className={`${styles['formContainerLogin']}`}
    >
      <h2>{isLogin ? 'Faça seu Login' : 'Cadastre seu Usuário'}</h2>
      <form className={styles.form} action='submit' onSubmit={loginHandler}>
        {isLogin ? (
          <Fragment>
            <Input
              type='email'
              InputType='text'
              id='loginEmail'
              placeholder='✉️ Digite seu e-mail'
              text='E-mail'
              onChange={typeEmailHandler}
              value={authValues.email}
            />
            <Input
              type='password'
              InputType='text'
              id='loginPassword'
              placeholder='🔑 Digite sua senha'
              text='Senha'
              onChange={typePasswordHandler}
              value={authValues.password}
            />
          </Fragment>
        ) : (
          <Fragment>
            <Input
            type='email'
            InputType='text'
            id='registerEmail'
            placeholder='✉️ Cadastre seu e-mail'
            text='E-mail'
            onChange={typeEmailHandler}
            value={authValues.email}
            />
            <Input
              type='password'
              InputType='text'
              id='loginPassword'
              placeholder='🔑 Cadastre sua senha'
              text='Senha'
              onChange={typePasswordHandler}
              value={authValues.password}
            />
          </Fragment>
        )}
        <div className={styles.buttonContainer}>
          <Link to='/'>
            <ArrowLeft size={24} />
            Voltar para Home
          </Link>
          {isLogin ? (
            <Button
              buttonType={`${
                loginFormIsFilled ? 'confirmButton' : 'disabledConfirmButton'
              }`}
              type='submit'
              disabled={!loginFormIsFilled}
            >
              Login
            </Button>
          ) : (
            <Button
              buttonType={`${
                registerFormIsFilled ? 'confirmButton' : 'disabledConfirmButton'
              }`}
              type='submit'
              disabled={!registerFormIsFilled}
            >
              Cadastrar
            </Button>
          )}
        </div>
      </form>
      <Button
        buttonType='navButton'
        type='button'
        onClick={changeFunctionHandler}
      >
        {isLogin
          ? 'Novo usuário? Faça seu cadastro!'
          : 'Mudar para tela de Login'}
      </Button>
    </div>
  );
}
