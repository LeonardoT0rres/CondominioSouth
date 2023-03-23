import { useState } from 'react';
import styles from './Schedules.module.css';
import { PencilLine, Trash } from 'phosphor-react';

import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router';
import { ScheduleValues } from '../../../../types';
import { Button } from '../../../UI/Button/Button';
import { scheduleDelete, schedulePatch } from '../../../../services/SchedulesService';
import { toast } from 'react-toastify';

type scheduleProps = {
  schedules: ScheduleValues;
};

export function Schedule({ schedules }: scheduleProps) {
  const navigate = useNavigate();

  const [start, setStart] = useState<Date>();

  const { id } = useParams();
  function editSchedule(Schedule: ScheduleValues) {
    navigate(`/schedule-edit/${id}/${schedules.id}`);
    // navigate('/assembleia-lista/descricao')
    // localStorage.setItem("assemblyDesc",JSON.stringify(assembly));
  }

  function openSchedule(Schedule: ScheduleValues) {
    navigate(`/votacao/${id}/${schedules.id}`);
    // navigate('/assembleia-lista/descricao')
    // localStorage.setItem("assemblyDesc",JSON.stringify(assembly));
  }

  function startAssembly() {
    const startVote: Date = new Date();
    startVote.toLocaleTimeString();
    schedulePatch(schedules.id, startVote);
  }

  function scheduleDeleteHandler() {
    try {
      scheduleDelete(schedules.id);
      toast.success('Pauta Deletada.', {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'scheduleDeleteSuccessMessage',
      });
    } catch {
      toast.error(`Que pena, não foi possível deletar a pauta`, {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'scheduleDeleteErrorMessage',
      });
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.assembly}>
        <header>
          <h2>{schedules.name}</h2>
          <Button
            buttonType={'formButton'}
            onClick={() => editSchedule(schedules)}
            id='EditSchedule'
          >
            <PencilLine size={32} />
          </Button>
          <Button buttonType={'formButton'} onClick={startAssembly}>
            {'Iniciar Pauta'}
          </Button>
          <Button buttonType={'formButton'} onClick={scheduleDeleteHandler}>
            <Trash size={32} />
          </Button>
        </header>
        <div onClick={() => openSchedule(schedules)}>
          <h3>{`Pauta de Número ${schedules.scheduleOrder}`}</h3>
          <p>
            <strong>Assunto:</strong>
            {` ${schedules.description}`}
          </p>
        </div>
      </div>
    </div>
  );
}
