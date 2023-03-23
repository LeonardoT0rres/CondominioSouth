import { ChangeEvent } from 'react';
import styles from './RadioButton.module.css';

interface RadioButtonProps {
  checked: boolean;
  value: string;
  name: string;
  id: string;
  onChange: (event: ChangeEvent<HTMLInputElement>) => void;
  text?: string;
}

export function RadioButton({ id, text, value, name, onChange }: RadioButtonProps) {
  return (
    <div className={styles['items__custom-radio']}>
      <input type='radio' id={id} value={value} name={name} onChange={onChange}/>
      <label htmlFor={id} />
      <span>{text}</span>
    </div>
  );
}
