import styles from './RestrictedAccessContent.module.css';

export function RestrictedAccessContent() {
  return (
    <div className={styles.restrictedContainer}>
      <article className={styles.restrictedText}>
        <p>Acesso restrito. Verifique se você realizou o login.</p>
        <p>Se já estiver logado, você só terá acesso à página de autenticação realizando logout.</p>
      </article>
    </div>
  );
}
