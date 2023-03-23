import { ChangeEvent, useEffect, useState } from 'react';
import { Button } from '../UI/Button/Button';
import { RadioButton } from '../UI/RadioButton/RadioButton';
import styles from './SchedulesVoteList.module.css';
import { toast } from 'react-toastify';
import { useParams } from 'react-router-dom';
import { AssemblyValues, ScheduleValues, vote } from '../../types';
import { scheduleGetUni } from '../../services/SchedulesService';
import { assemblyGetUni } from '../../services/AssembliesService';
import { votePost } from '../../services/vote';
import { useTimer } from 'react-timer-hook';
import { MyTimer } from '../UI/Timer/Index';

export function SchedulesVoteList() {

  //voto ficticios para teste

  const [assemblyValues, setAssemblyValues] = useState<AssemblyValues>({
    name: '',
    locale: '',
    obs: '',
    start: '',
    cardinality: '',
  });
  const [schedulesValues, setSchedulesValues] = useState<ScheduleValues>({
    name: '',
    description: '',
    scheduleOrder: '',
    status:'',
    negativeVotes: 0,
    positiveVotes: 0,  
  });

  const time = new Date();
  time.setSeconds(time.getSeconds() + 60);
  
  const [vote, setVote] = useState<vote>({
    vote: ''
  })
  const [ConfirmVote, setConfirmVote] = useState<boolean>(false)
  const [visivel, setVisivel] = useState<boolean>(false)
  const [visivelResult, setVisivelResult] = useState<boolean>()

  function confirmVoteHandler(event: ChangeEvent) {
    VoteSchedule(vote)
    console.log(vote)
    event.preventDefault();
    setConfirmVote(true)
    try {
      toast.success('Voto Confirmado!', {
        position: toast.POSITION.TOP_CENTER,
        toastId: 'ConfirmVoteMessage',
      });
    } catch (error: any) {
      toast.error(
        `Houve um erro ao confirmar o voto: ${error.message}`,
        {
          position: toast.POSITION.TOP_CENTER,
          toastId: 'VoteErrorMessage',
        }
      );
    }
  }

  const { idAssembly, idSchedule } = useParams();
  async function loadAssemblyData() {
    const response = await assemblyGetUni(idAssembly);
    setAssemblyValues(response?.data);
  }
  const CarregarSchedule = async () => {
    const response = await scheduleGetUni(idAssembly,idSchedule)
    setSchedulesValues(response?.data)
    if(schedulesValues.status === "ANDAMENTO"){
      setVisivel(true)
    } else {
      setVisivel(false)
    }
  }
  
  const VoteSchedule = async (vote: vote) => {
    try {
      await votePost(idSchedule, vote);
    } catch (error: any) {}
    setVote({vote: ''})
  }

  useEffect(()=>{
    loadAssemblyData()
    CarregarSchedule()
  },[])

  const radioHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
    setVote({vote: event.target.value})
  }

  let votoSim: number | undefined 
  let votoNao: number | undefined 
  let votoTotais: number 
  if(schedulesValues.negativeVotes !== undefined){
    votoNao = schedulesValues.negativeVotes
  } else { votoNao = 0}
  if(schedulesValues.positiveVotes !== undefined){
    votoSim = schedulesValues.positiveVotes
  } else { votoSim = 0}
  if(votoNao === 0 && votoSim === 0){
    votoTotais = 1
  }else{votoTotais = votoSim + votoNao}

  let radioIsSelected = false;
  if(vote.vote !== '' && !ConfirmVote){
     radioIsSelected = true;
  } else {radioIsSelected = false; }

  let status = schedulesValues.status
  return (
    <div className={styles.mainContainer}>
      <h2>Assembleia {assemblyValues.name}</h2>
      <div className={styles.scheduleVoteContainer}>
        <ul>
          <li>
            <div className={styles.textContent}>
              <h3>{schedulesValues.name}:</h3>
              <p>{schedulesValues.description}</p>
            </div>
            <fieldset disabled={ConfirmVote}>
              <RadioButton
                 checked={false}
                 id={'Sim'}
                 text={'Sim'}
                 value={'true'}
                 name={'vote'} 
                 onChange={radioHandler}               
              />
              <RadioButton
                  checked={false}
                  id={'Não'}
                  text={'Não'}
                  value={'false'}
                  name={'vote'} 
                  onChange={radioHandler}
              />
            </fieldset>
            <div>
              <span>{`Status: ${schedulesValues.status}`/*aguardando adição do back*/}</span>
            </div>
          </li>
        </ul>
            <Button
          buttonType={`${
            radioIsSelected ? 'confirmButton' : 'disabledConfirmButton'
          }`}
          disabled={radioIsSelected}
          type='button'
          onClick={confirmVoteHandler}
        >
          Confirmar
        </Button>
        <div >
          <div style={{flexDirection:'row'}}>
              <span style={{alignItems:'left' ,marginLeft:'4px'}}>Sim</span>
              <span style={{float:'right', marginLeft:'340px'}}>Não</span>
            </div>
            <div className={styles.Parentdiv}>
              <span className={styles.progresstext} style={{float:'right'}}>{`${(votoNao * 100)/votoTotais}%`}</span>
              <div className={styles.Childdiv} style={{width:`${(votoSim * 100)/votoTotais}%`}}>
                <span className={styles.progresstext}>{`${(votoSim * 100)/votoTotais}%`}</span>
              </div>
            </div>
            <span style={{alignItems:'left' ,marginLeft:'4px'}}>{votoSim}</span>
              <span style={{float:'right', marginLeft:'340px'}}>{votoNao}</span>
            <div style={{textAlign:'center'}}>
              <span>{schedulesValues.status}</span>
            </div>
            <div hidden={!visivel}>
              <MyTimer expiryTimestamp={time} />
            </div>
          </div>
        </div>
    </div>
  );
}
