import styles from './ListAssembly.module.css';
import { Button } from '../UI/Button/Button';
import { Assembly } from './Assembleia/Assembly';
import { useNavigate } from 'react-router-dom';
import { AssemblyValues } from '../../types';
import { useEffect, useState } from 'react';
import { assemblyGet } from '../../services/AssembliesService';
import { Input } from '../UI/Input/Input';
import { RadioButton } from 'phosphor-react';

export function ListAssembly() {
  const navigate = useNavigate();
  const [assemblyValues, setAssemblyValues] = useState<AssemblyValues[]>([
    {
      name: 'Assemblei para discutir melhorias no condominio',
      locale: 'Auditório',
      obs: 'Assembleia sobre a contruçãoda nova pscina no condominio, além da contrução de uma quadra de tênis um campo de golf e um lago artificial para carpascontruçãoda nova pscina no condominio, além da contrução de uma quadra de tênis um campo de golf e um lago artificial para carpascontruçãoda nova pscina no condominio, além da contrução de uma quadra de tênis um campo de golf e um lago artificial para carpascontruçãoda nova pscina no condominio, além da contrução de uma quadra de tênis um campo de golf e um lago artificial para carpas',
      start: 'Auditório no dia 20/02/2022 as 18:30 ',
      cardinality: '3º',
    },
  ]);
  const [state, setState] = useState<any>([])

  const CarregarAssembleias = async () => {
    const response = await assemblyGet();
    setAssemblyValues(response?.data);
  };

  const handleCadastro = () => {
    navigate('/assembleia-cadastro');
  };
  useEffect(() => {
    CarregarAssembleias();
  }, []);

  return (
    <div className={styles.container}>
      <header>
        <h1>Lista de assembleias</h1>
        <Button buttonType={'formButton'} onClick={handleCadastro}>
          Nova Assembleia
        </Button>
      </header>
      <div className={styles.lista}>
        {assemblyValues?.map?.((assembly, index) => (
          <Assembly key={index} assembly={assembly} />
        ))}
      </div>
    </div>
  );
}
