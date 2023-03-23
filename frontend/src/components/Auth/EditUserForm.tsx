import { ChangeEvent, FormEvent, useContext, useEffect, useState } from 'react';

import { Button } from '../UI/Button/Button';
import { Input } from '../UI/Input/Input';
import { useNavigate } from 'react-router-dom';

import { AuthValues } from '../../types';
import { loginPost } from '../../services/AuthService';

import styles from './EditUserForm.module.css';
import { toast } from 'react-toastify';

import AuthContext from '../../services/context/auth-context';

// ESTE COMPONENTE A PRINCÍPIO DEVE SER ACESSADO SOMENTE PELO LINK NO "/resident", para ter a permissão limitada de Residente
export function EditUserForm() {
  const [editValues, setEditValues] = useState<AuthValues>({
    name: '',
    email: '',
    apartmentNumber: '',
    password: '',
  });
  // Redireciona para form com Nome, e-mail e senha

  const authCtx = useContext(AuthContext);
  const navigate = useNavigate();

  function typeEmailHandler(event: ChangeEvent<HTMLInputElement>) {
    setEditValues({ ...editValues, email: event.target.value });
  }

  function typePasswordHandler(event: ChangeEvent<HTMLInputElement>) {
    setEditValues({ ...editValues, password: event.target.value });
  }

  async function registerHandler(event: FormEvent) {
    event.preventDefault();

    // Provisório até ver como vai ser a validação pelo backend em vez de com o sistema do firebase, que já tem uma validação mínima!
    const authDataIsValid = true;

    if (authDataIsValid) {
      try {
        const response = await loginPost(editValues);

        const data = response.data;

        // --------------------------------

        toast.success(
          'Morador Registrado! Você pode realizar seu login agora :D',
          {
            position: toast.POSITION.TOP_CENTER,
            toastId: 'editUserSuccessMessage',
          }
        );

        navigate('/resident');
      } catch (error: any) {
        toast.error(
          `Que pena, não foi possível realizar o registro de morador: ${error.message}`,
          {
            position: toast.POSITION.TOP_CENTER,
            toastId: 'editUserErrorMessage',
          }
        );
      }
    } else {
      toast.warn('Dados inválidos. Por favor, verifique as entradas', {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'editUserWarningMessage',
      });
    }

    setEditValues({
      email: '',
      password: '',
    });
  }

  const formIsFilled = editValues.email && editValues.password ? true : false;

  return (
    <div className={styles.formContainerLogin}>
      <h2>
        Condômino, conclua seu cadastro com seu e-mail fornecido ao síndico:
      </h2>
      <form className={styles.form} action='submit' onSubmit={registerHandler}>
        <Input
          type='text'
          InputType='text'
          id='registerName'
          placeholder='📝 Digite o nome do morador'
          text='Nome'
          onChange={typeEmailHandler}
          value={editValues.name}
        />
        <Input
          type='email'
          InputType='text'
          id='registerEmail'
          placeholder='✉️ Digite o e-mail cadastrado'
          text='E-mail'
          onChange={typeEmailHandler}
          value={editValues.email}
        />
        <Input
          type='text'
          InputType='text'
          id='registerApartment'
          placeholder='🏠 Digite o número do Apartamento'
          text='Apartamento'
          onChange={typeEmailHandler}
          value={editValues.apartmentNumber}
        />
        <Input
          type='password'
          InputType='text'
          id='registerPassword'
          placeholder='🔑 Cadastre a senha'
          text='Senha'
          onChange={typePasswordHandler}
          value={editValues.password}
        />
        <div className={styles.buttonContainer}>
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
