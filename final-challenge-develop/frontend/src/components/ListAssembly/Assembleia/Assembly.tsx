import styles from './Assembly.module.css';
import { Button } from '../../UI/Button/Button';
import { AssemblyValues } from '../../../types';
import { useNavigate } from 'react-router';
import { dateFormat } from '../../../services/DateFormatter';
import { assemblyPatch } from '../../../services/AssembliesService';

type assemblyProps = {
  assembly: AssemblyValues;
};
export let assemblyDesc: AssemblyValues;

export function Assembly({ assembly }: assemblyProps) {
  const navigate = useNavigate();

  function openAssembly(assembly: AssemblyValues) {
    navigate(`descricao/${assembly.id}`);
  }
  function endAssembly() {
    const startVote: Date = new Date()
    startVote.toLocaleTimeString()
    assemblyPatch(assembly.id, startVote)
  }

  return (
    <div className={styles.assembly}>
      <header>
        <div className={styles.leftSide} onClick={() => openAssembly(assembly)}>
          <h2>{assembly.name}</h2>
          <p>{`Assembleia ${assembly.cardinality}`}</p>
          <p>{` Marcada para: ${dateFormat(assembly.start)}`}</p>
          <p>{`Local: ${assembly.locale} `}</p>
        </div>
        <div className={styles.rightSide}>
          <Button buttonType={'formButton'} onClick={endAssembly}>
            {'Encerrar Assembleia'}
          </Button>
          <p style={{ float: 'right' }}>{`Status: ${assembly.status}`}</p>
        </div>
      </header>
    </div>
  );
}
