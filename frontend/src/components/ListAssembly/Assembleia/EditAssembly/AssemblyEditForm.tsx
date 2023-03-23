import { ArrowLeft } from 'phosphor-react';
import { ChangeEvent, FormEvent, useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { AssemblyValues, InvalidValues } from '../../../../types';
import { Button } from '../../../UI/Button/Button';
import { Input } from '../../../UI/Input/Input';

import styles from './AssemblyEditForm.module.css';
import {
  assemblyDataIsValid,
  lengthIsValid,
  dateisValid,
  itemOrderIsValid,
} from '../../../../services/FormValidation';
import {
  assemblyGet,
  assemblyGetUni,
  assemblyPut,
} from '../../../../services/AssembliesService';
import { dateFormat, dateYearDigitCorrector } from '../../../../services/DateFormatter';

export function AssemblyEditForm() {

  const d1 = new Date();
  const d2 = (new Date(d1.valueOf() - d1.getTimezoneOffset() * 60000)).toISOString();

  const [assembliesValues, setAssembliesValues] = useState<AssemblyValues>({
    name: '',
    locale: '',
    obs: '',
    start: d2,
    cardinality: '',
  });

  const { id } = useParams();
  const CarregarAssembleias = async () => {
    const response = await assemblyGetUni(id);
    setAssembliesValues(response?.data);
  };

  useEffect(() => {
    CarregarAssembleias();
  }, []);

  const [validationError, setValidationError] = useState<InvalidValues>({
    invalidName: false,
    invalidDescription: false,
    invalidOrder: false,
  });

  const navigate = useNavigate();

  function typeNameHandler(event: ChangeEvent<HTMLInputElement>) {
    setAssembliesValues({ ...assembliesValues, name: event.target.value });
  }

  function nameValidationHandler() {
    setValidationError({
      ...validationError,
      invalidName: !lengthIsValid(assembliesValues.name, 3),
    });
  }

  function typeLocaleHandler(event: ChangeEvent<HTMLInputElement>) {
    setAssembliesValues({ ...assembliesValues, locale: event.target.value });
  }

  function localeValidationHandler() {
    setValidationError({
      ...validationError,
      invalidLocale: !lengthIsValid(assembliesValues.locale, 8),
    });
  }

  function typeDescriptionHandler(event: ChangeEvent<HTMLInputElement>) {
    setAssembliesValues({ ...assembliesValues, obs: event.target.value });
  }

  function descriptionValidationHandler() {
    setValidationError({
      ...validationError,
      invalidDescription: !lengthIsValid(assembliesValues.obs, 20),
    });
  }

  function typeDateAndTimeHandler(event: ChangeEvent<HTMLInputElement>) {
    let correctDate = event.target.value;

    setAssembliesValues({ ...assembliesValues, start: dateYearDigitCorrector(correctDate) });
  }

  function dateAndTimeValidationHandler() {
    setValidationError({
      ...validationError,
      invalidStart: !dateisValid(assembliesValues.start),
    });
  }

  function typeAssemblyNumberHandler(event: ChangeEvent<HTMLInputElement>) {
    setAssembliesValues({
      ...assembliesValues,
      cardinality: event.target.value,
    });
  }

  function assemblyNumberValidationHandler() {
    setValidationError({
      ...validationError,
      invalidOrder: !itemOrderIsValid(assembliesValues.cardinality),
    });
  }

  async function registerAssemblyHandler(event: FormEvent) {
    event.preventDefault();

    if (assemblyDataIsValid(assembliesValues)) {
      try {
        await assemblyPut(assembliesValues);
        toast.success('Assembleia Atualizada!', {
          position: toast.POSITION.TOP_CENTER,
          toastId: 'assemblyEditSuccessMessage',
        });
        navigate('/assembleia-lista');
      } catch (error: any) {
        toast.error(`Ocorreu um erro: ${error.message}`, {
          position: toast.POSITION.TOP_CENTER,
          toastId: 'assemblyEditErrorMessage',
        });
      }
    } else {
      toast.warn('Dados inválidos! Verifique as entradas', {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'assemblyEditWarningMessage',
      });
    }
  }

  const formIsFilled =
    assembliesValues.name &&
    assembliesValues.locale &&
    assembliesValues.obs &&
    assembliesValues.start &&
    assembliesValues.cardinality
      ? true
      : false;

  return (
    <div className={styles.formContainer}>
      <h2>Edição da Assembleia:</h2>
      <form
        className={styles.form}
        action='submit'
        onSubmit={registerAssemblyHandler}
      >
        <Input
          type='text'
          InputType='text'
          id='tituloAssembleia'
          placeholder='Digite o Título da Assembleia'
          text='Título'
          onChange={typeNameHandler}
          onBlur={nameValidationHandler}
          value={assembliesValues.name}
          error={validationError.invalidName}
          errorMessage={'Nome deve ter mais que 3 caracteres'}
        ></Input>
        <Input
          type='text'
          InputType='text'
          id='localeAssembleia'
          placeholder='Digite o Local da Assembleia'
          text='Local'
          onChange={typeLocaleHandler}
          value={assembliesValues.locale}
          onBlur={localeValidationHandler}
          error={validationError.invalidLocale}
          errorMessage={'O local de assembleia deve ter mais que 8 caracteres'}
        />
        <Input
          type='text'
          InputType='text'
          id='descricaoAssembleia'
          placeholder='Digite a Descrição da Assembleia'
          text='Descrição'
          onChange={typeDescriptionHandler}
          value={assembliesValues.obs}
          onBlur={descriptionValidationHandler}
          error={validationError.invalidDescription}
          errorMessage={'Descrição deve ter mais que 20 caracteres'}
        />
        <Input
          type='datetime-local'
          InputType='text'
          id='dataHorarioAssembleia'
          placeholder=''
          text='Data e Horário'
          onChange={typeDateAndTimeHandler}
          value={assembliesValues.start}
          onBlur={dateAndTimeValidationHandler}
          error={validationError.invalidStart}
          errorMessage={
            'Data e hora precisam ser futuros (até o ano que vem no máximo)'
          }
          min={new Date().toISOString()}
          max='2023-12-31T23:59'
          pattern='[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}'
        />
        <Input
          type='number'
          InputType='text'
          id='numeroAssembleia'
          placeholder='Ex: 5'
          text='Número da Assembleia:'
          onChange={typeAssemblyNumberHandler}
          value={assembliesValues.cardinality}
          onBlur={assemblyNumberValidationHandler}
          error={validationError.invalidOrder}
          errorMessage={'O número da assembleia deve ser positivo'}
        />
        <div className={styles.buttonContainer}>
          <Link to='assembleia-lista/descricao'>
            <ArrowLeft size={24} />
            Voltar
          </Link>
          <Button
            buttonType={`${
              formIsFilled ? 'confirmButton' : 'disabledConfirmButton'
            }`}
            type='submit'
            disabled={!formIsFilled}
            id="ConfirmEditAssembly"
          >
            Salvar
          </Button>
        </div>
      </form>
    </div>
  );
}
