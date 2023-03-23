import styles from './DescriptionAssembly.module.css';
import { Button } from '../../../UI/Button/Button';
import { useNavigate, useParams } from 'react-router-dom';
import { AssemblyValues, ScheduleValues } from '../../../../types';
import { useEffect, useState } from 'react';
import { ArrowLeft, NotePencil, PencilLine, Trash } from 'phosphor-react';
import { Schedule } from '../ListSchedules/Schedules';
import { scheduleGet } from '../../../../services/SchedulesService';
import {
  assemblyDelete,
  assemblyGetUni,
} from '../../../../services/AssembliesService';
import { toast } from 'react-toastify';
import { dateFormat } from '../../../../services/DateFormatter';

export function DescriptionAssembly() {
  // let data:AssemblyValues
  // let assembly = localStorage.getItem("assemblyDesc");
  // data = JSON.parse(JSON.stringify(assembly))

  /**name: 'Assemblei para discutir melhorias no condominio',
            locale: 'Auditório',
            obs: 'Assembleia sobre a contruçãoda nova pscina no condominio, além da contrução de uma quadra de tênis um campo de golf e um lago artificial para carpascontruçãoda nova pscina no condominio, além da contrução de uma quadra de tênis um campo de golf e um lago artificial para carpascontruçãoda nova pscina no condominio, além da contrução de uma quadra de tênis um campo de golf e um lago artificial para carpascontruçãoda nova pscina no condominio, além da contrução de uma quadra de tênis um campo de golf e um lago artificial para carpas',
            start: 'Auditório no dia 20/02/2022 as 18:30 ',
            cardinality: '3º', */

  const [assemblyValues, setAssemblyValues] = useState<AssemblyValues>({
    name: '',
    locale: '',
    obs: '',
    start: '',
    cardinality: '',
  });
  const [stateSchedule, setStateSchedule] = useState<ScheduleValues[]>([
    {
      name: '',
      description: '',
      scheduleOrder: '',
    },
  ]);

  const { id } = useParams();
  const handleCadastrarPautas = () => {
    navigate(`/pauta-cadastro/${id}`);
  };

  async function loadSchedules() {
    const response = await scheduleGet(id);
    setStateSchedule(response?.data);
  }

  async function loadAssemblyData() {
    const response = await assemblyGetUni(id);
    setAssemblyValues(response?.data);
  }

  useEffect(() => {
    loadSchedules();
    loadAssemblyData();
  }, []);

  const navigate = useNavigate();

  const handleDelete = async () => {
    try {
      await assemblyDelete(id);
      toast.success('Assembleia Excluída.', {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'assemblyDeleteSuccessMessage',
      });
      navigate('/assembleia-lista');
    } catch (error: any) {
      toast.error(`Não foi possível deletar a assembleia: ${error.message}`, {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'assemblyDeleteErrorMessage',
      });
    }
  };

  const handleBack = () => {
    navigate('/assembleia-lista');
  };

  const handleEdit = () => {
    navigate(`/assembleia-edit/${id}`);
  };

  return (
    <div className={styles.container}>
      <div className={styles.assemblyContainer}>
        <header className={styles.assemblyHeader}>
          <h1>Informações da assembleia </h1>
          <div className={styles.botoes}>
            <Button buttonType={'formButton'} onClick={handleEdit}>
              <PencilLine size={32} />
            </Button>
            <Button buttonType={'formButton'} onClick={handleDelete}>
              <Trash size={32} />
            </Button>
            <Button buttonType={'formButton'} onClick={handleCadastrarPautas}>
              Nova Pauta
              <NotePencil size={32} />
            </Button>
            <Button buttonType={'formButton'} onClick={handleBack}>
              <ArrowLeft size={32} />
            </Button>
          </div>
        </header>
        <div className={styles.description}>
          <h3>Título da Assembleia</h3>
          <label>{assemblyValues.name}</label>
          <hr />
          <h3>Assembleia número</h3>
          <label>{assemblyValues.cardinality}</label>
          <hr />
          <h3>Local</h3>
          <label>{assemblyValues.locale}</label>
          <hr />
          <h3>Data e hora</h3>
          <label>{dateFormat(assemblyValues.start)}</label>
          <hr />
          <h3>Descrição</h3>
          <label>{assemblyValues.obs}</label>
          <hr />
          <h3>Status</h3>
          <label>{assemblyValues.status}</label>
        </div>
      </div>
      <div className={styles.schedulesContainer}>
        <div
          className={`${stateSchedule.length ? styles.lista : styles.listaVazia}`}
        >
          {stateSchedule?.map?.((schedules, index) => (
            <Schedule key={index} schedules={schedules} />
          ))}
        </div>
      </div>
    </div>
  );
}
