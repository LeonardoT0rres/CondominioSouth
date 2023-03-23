import { ArrowLeft } from 'phosphor-react';
import { ChangeEvent, FormEvent, useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import {
  itemOrderIsValid,
  lengthIsValid,
  scheduleDataIsValid,
} from '../../../../../services/FormValidation';
import { scheduleGetUni, schedulePut } from '../../../../../services/SchedulesService';
import { InvalidValues, ScheduleValues } from '../../../../../types';
import { Button } from '../../../../UI/Button/Button';
import { Input } from '../../../../UI/Input/Input';

import styles from './ScheduleEditForm.module.css';

export function ScheduleEditForm() {
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

  const { idAssembly, idSchedule } = useParams();
  const CarregarSchedule = async () => {
    const response = await scheduleGetUni(idAssembly,idSchedule)
    console.log(response?.data)
    setSchedulesValues(response?.data)
  }
  useEffect(() => {
    CarregarSchedule()
  },[])


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

  async function EditScheduleHandler(event: FormEvent) {
    event.preventDefault();
    console.log(schedulesValues)

    if (scheduleDataIsValid(schedulesValues)) {
      try {
        const response = await schedulePut(schedulesValues);
        toast.success('Pauta Atualizada!', {
          position: toast.POSITION.TOP_CENTER,
          toastId: 'scheduleEditSuccessMessage',
        });
        navigate(`/assembleia-lista/descricao/${idAssembly}`);
      } catch (error: any) {
        toast.error(`Ocorreu um erro: ${error.message}`, {
          position: toast.POSITION.TOP_CENTER,
          toastId: 'scheduleEditErrorMessage',
        });
      }
    } else {
      toast.warn('Dados inválidos! Verifique as entradas', {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'scheduleEditWarningMessage',
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
      <h2>Edição da Pauta:</h2>
      <form
        className={styles.form}
        action='submit'
        onSubmit={EditScheduleHandler}
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
          <Link to={`/assembleia-lista/descricao/${idAssembly}`}>
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
            Salvar
          </Button>
          <ToastContainer />
        </div>
      </form>
    </div>
  );
}
