import { ButtonHTMLAttributes } from 'react';
import styles from './Button.module.css';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  buttonType: string;
  children: React.ReactNode;
  id?: string;
  disabled?: boolean;
  onClick?: (event?: any) => void;
}

export function Button({ children, buttonType, disabled = false, type, ...rest }: ButtonProps) {
  return <button disabled={disabled} className={styles[`${buttonType}`]} {...rest}>{children}</button>;
}
