import { Buildings } from 'phosphor-react';
import { useContext } from 'react';
import { Link, redirect } from 'react-router-dom';
import AuthContext from '../../services/context/auth-context';
import { Button } from '../UI/Button/Button';

import styles from './MainHeader.module.css';

export function MainHeader() {
  const authCtx = useContext(AuthContext);

  const logoutHandler = () => {
    authCtx.logout();
    redirect('/');
  };

  return (
    <header className={styles.header}>
      <Link to='/'>
        <Buildings size={32} />
        <div>Condomínio South</div>
      </Link>
      <nav>
        {authCtx.userIsLoggedIn && (
          <ul>
            <li>
              <Link to='/assembleia-lista' title='Lista de Assembleias'>
                Lista de Assembleias
              </Link>
            </li>
            <li>
              <Button
                buttonType='confirmButton'
                onClick={logoutHandler}
                type='button'
              >
                Logout
              </Button>
            </li>
          </ul>
        )}

        {!authCtx.userIsLoggedIn && (
          <ul>
            <li>
              <Link to='/auth' title='Cadastro e Login'>
                Login
              </Link>
            </li>
          </ul>
        )}
      </nav>
    </header>
  );
}
