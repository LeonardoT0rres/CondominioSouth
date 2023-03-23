import { ArrowLeft } from 'phosphor-react';
import { ChangeEvent, FormEvent, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import {
  itemOrderIsValid,
  lengthIsValid,
  scheduleDataIsValid,
} from '../../services/FormValidation';
import { schedulePost } from '../../services/SchedulesService';
import { InvalidValues, ScheduleValues } from '../../types';
import { Button } from '../UI/Button/Button';
import { Input } from '../UI/Input/Input';

import styles from './ScheduleRegisterForm.module.css';

export function ScheduleRegisterForm() {
  const [schedulesValues, setSchedulesValues] = useState<ScheduleValues>({
    name: '',
    description: '',
    scheduleOrder: '',
  });

  const [validationError, setValidationError] = useState<InvalidValues>({
    invalidName: false,
    invalidDescription: false,
    invalidOrder: false,
  });

  const navigate = useNavigate();

  const { id } = useParams();

  function typeNameHandler(event: ChangeEvent<HTMLInputElement>) {
    setSchedulesValues({ ...schedulesValues, name: event.target.value });
  }

  function typeDescriptionHandler(event: ChangeEvent<HTMLInputElement>) {
    setSchedulesValues({ ...schedulesValues, description: event.target.value });
  }

  function typeScheduleOrderHandler(event: ChangeEvent<HTMLInputElement>) {
    setSchedulesValues({
      ...schedulesValues,
      scheduleOrder: event.target.value,
    });
  }

  function orderValidationHandler() {
    setValidationError({
      ...validationError,
      invalidOrder: !itemOrderIsValid(schedulesValues.scheduleOrder),
    });
  }

  function validateTexts(inputValue: string, length: number) {
    if (inputValue === schedulesValues.name) {
      setValidationError({
        ...validationError,
        invalidName: !lengthIsValid(inputValue, length),
      });
    } else if (inputValue === schedulesValues.description) {
      setValidationError({
        ...validationError,
        invalidDescription: !lengthIsValid(inputValue, length),
      });
    }
  }

  async function registerScheduleHandler(event: FormEvent) {
    event.preventDefault();

    if (scheduleDataIsValid(schedulesValues)) {
      try {
        const response = await schedulePost(schedulesValues, id);
        toast.success('Pauta Cadastrada!', {
          position: toast.POSITION.TOP_CENTER,
          toastId: 'scheduleSuccessMessage',
        });
        navigate(`/assembleia-lista/descricao/${id}`);
      } catch (error: any) {
        toast.error(
          `Que pena, não foi possível cadastrar a pauta: ${error.message}`,
          {
            position: toast.POSITION.TOP_CENTER,
            toastId: 'scheduleErrorMessage',
          }
        );
      }
    } else {
      toast.warn('Dados inválidos. Por favor, verifique as entradas', {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'scheduleWarningMessage',
      });
    }
  }

  const formIsFilled =
    schedulesValues.name &&
    schedulesValues.description &&
    schedulesValues.scheduleOrder
      ? true
      : false;

  return (
    <div className={styles.formContainer}>
      <h2>Cadastre uma Pauta:</h2>
      <form
        className={styles.form}
        action='submit'
        onSubmit={registerScheduleHandler}
      >
        <Input
          type='text'
          InputType='text'
          id='scheduleName'
          placeholder='Digite o Título da Pauta'
          text='Título'
          onChange={typeNameHandler}
          onBlur={() => validateTexts(schedulesValues.name, 3)}
          value={schedulesValues.name}
          error={validationError.invalidName}
          errorMessage={'Nome deve ter mais que 3 caracteres'}
        />
        <Input
          type='text'
          InputType='text'
          id='scheduleDescription'
          placeholder='Digite a Descrição da Pauta'
          text='Descrição'
          onChange={typeDescriptionHandler}
          onBlur={() => validateTexts(schedulesValues.description, 20)}
          value={schedulesValues.description}
          error={validationError.invalidDescription}
          errorMessage={'Descrição deve ter mais que 20 caracteres'}
        />
        <Input
          type='number'
          InputType='text'
          id='scheduleNumber'
          placeholder='Ex: 5'
          text='Número da Pauta'
          onChange={typeScheduleOrderHandler}
          onBlur={orderValidationHandler}
          value={schedulesValues.scheduleOrder}
          error={validationError.invalidOrder}
          errorMessage={'O número precisa ser positivo'}
        />
        <div className={styles.buttonContainer}>
          <Link to='/assembleia-lista/descricao'>
            <ArrowLeft size={24} />
            Voltar para Lista
          </Link>
          <Button
            buttonType={`${
              formIsFilled ? 'confirmButton' : 'disabledConfirmButton'
            }`}
            type='submit'
            disabled={!formIsFilled}
          >
            Cadastrar Pauta
          </Button>
        </div>
      </form>
    </div>
  );
}
