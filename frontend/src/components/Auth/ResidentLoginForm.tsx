import { ChangeEvent, FormEvent, useContext, useState } from 'react';

import { Button } from '../UI/Button/Button';
import { Input } from '../UI/Input/Input';
import { Link, useNavigate } from 'react-router-dom';

import { AuthValues } from '../../types';
import { loginPost } from '../../services/AuthService';

import styles from './ResidentLoginForm.module.css';
import { toast } from 'react-toastify';

import AuthContext from '../../services/context/auth-context';
import { ArrowLeft } from 'phosphor-react';

export function ResidentLoginForm() {
  const [authValues, setAuthValues] = useState<AuthValues>({
    email: '',
    password: '',
  });

  const authCtx = useContext(AuthContext);
  const navigate = useNavigate();

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
      try {
        const response = await loginPost(authValues);

        const data = response.data;
        // [TEMPORÁRIO] Lógica login/token do auth-context COM Firebase (com axios não deu pra pegar o expiresIn, portanto settei 60kk milisegundos = 60k segundos = 1k minutos, se calculei certo... deve dar mais da metade de um dia):
        const expirationTime: Date = new Date(
          new Date().getTime() + 60000 * 1000 // só para lembrar que é ms
        );

        // console.log(expirationTime);
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

  const formIsFilled = authValues.email && authValues.password ? true : false;

  return (
    <div className={styles.formContainerLogin}>
      <h2>
        Condômino, pode fazer seu login abaixo se tiver concluído seu cadastro:
      </h2>
      <form className={styles.form} action='submit' onSubmit={loginHandler}>
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
        <div className={styles.buttonContainer}>
          <Link to='/edit-user-page'>
            <ArrowLeft size={24} />
            Editar Cadastro
          </Link>
          <Button
            buttonType={`${
              formIsFilled ? 'confirmButton' : 'disabledConfirmButton'
            }`}
            type='submit'
            disabled={!formIsFilled}
          >
            Login
          </Button>
        </div>
      </form>
    </div>
  );
}
