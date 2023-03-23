import { useTimer } from 'react-timer-hook';

export function MyTimer({ expiryTimestamp}: any ) {
  const {
    seconds,
    minutes,
  } = useTimer({ expiryTimestamp, autoStart: true, onExpire: () => console.warn('onExpire called') });


  return (
    <div style={{textAlign: 'center'}}>
      <div style={{fontSize: '100px'}}>
        <span>{minutes.toString().padStart(2, '0')}</span>:<span>{seconds.toString().padStart(2, '0')}</span>
      </div>
    </div>
  );
}