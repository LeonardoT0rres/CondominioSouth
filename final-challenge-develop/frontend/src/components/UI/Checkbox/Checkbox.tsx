import styles from './Checkbox.module.css';

interface CheckboxProps {
  checked: boolean;
  id: string;
  onChange: () => void;
  text?: string
}

export function Checkbox({id, text}: CheckboxProps) {
  return (
    <div className={styles['items__custom-checkbox']}>
        <input
          type='checkbox'
          id={id}
        />
        <label htmlFor={id} />
        <span>{text}</span>
    </div>
  )
}
