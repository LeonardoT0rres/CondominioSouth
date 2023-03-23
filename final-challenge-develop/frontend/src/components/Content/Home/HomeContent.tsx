import styles from './HomeContent.module.css';

export function HomeContent() {
  return (
    <div className={styles.homeContainer}>
      <article className={styles.homeText}>
      <p>Bem-Vindo ao portal de Assembleias e Pautas do Condomínio South!</p>
        <p>Se já estiver logado, verifique as pautas.</p>
        <p>
          Caso contrário, pode realizar seu cadastro ou login no cabeçalho do site
        </p>
      </article>
    </div>
  );
}
