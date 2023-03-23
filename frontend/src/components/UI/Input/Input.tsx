import { ChangeEvent, InputHTMLAttributes } from 'react';
import styles from './Input.module.css';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  InputType: string;
  type: string;
  text:string;
  id: string;
  placeholder:string;
  value: string | undefined;
  error?: boolean;
  errorMessage?: string;
  onChange: (event: ChangeEvent<HTMLInputElement>) => void;
  onBlur?: () => void;
}

export function Input({InputType, type, text, id, placeholder, value, error, errorMessage, onChange, onBlur}: InputProps) {
  return (
    <div className={styles[`${InputType}`]} >
      <label htmlFor={id}>{text}</label>
      <input 
        type={type} 
        id={id} 
        placeholder={placeholder} 
        value={value}
        onChange={onChange} 
        onBlur={onBlur}
        />
      {error && <span className={styles.inputError}>{errorMessage}</span> }
    </div>
  )
}