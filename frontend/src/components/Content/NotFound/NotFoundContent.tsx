import styles from './NotFoundContent.module.css';

export function NotFoundContent() {
  return (
    <div className={styles.notFoundContainer}>
      <article className={styles.notFoundText}>
        <p>Página não encontrada!</p>
        <p>Verifique o endereço digitado após a barra na url.</p>
        <p>
          Por favor, procure navegar através dos links, sem alterar diretamente a url.
        </p>
      </article>
    </div>
  );
}
