import { Routes, Route } from 'react-router-dom';
import { Layout } from './components/Layout/Layout';
import { Home } from './pages/Home';
import { AuthPage } from './pages/Auth';
import { ListAssemblyPage } from './pages/ListAssembly';
import { AssemblyRegister } from './pages/AssemblyRegister';
import { ScheduleRegister } from './pages/ScheduleRegister';
import { PageNotFound } from './pages/PageNotFound';
import { RestrictedAccess } from './pages/RestrictedAccess';
import { SchedulesVoteList } from './components/SchedulesVoteList/SchedulesVoteList';
import { DescriptionAssembly } from './components/ListAssembly/Assembleia/DescricaoListaDePautas/DescriptionAssembly';
import { AssemblyEditPage } from './pages/EditAssembly';
import { ScheduleEditPage } from './pages/EditSchedules';
import { useContext } from 'react';
import AuthContext from './services/context/auth-context';
import { ResidentAuthPage } from './pages/ResidentAuthPage';
import { EditUserPage } from './pages/EditUserPage';

export function Router() {
  const authCtx = useContext(AuthContext);

  return (
    <Routes>
      <Route path='/' element={<Layout />}>
        <Route path='/' element={<Home />} />

        <Route
          path='/auth'
          element={
            authCtx.userIsLoggedIn ? (
              <RestrictedAccess />
            ) : (
              <AuthPage />
            )
          }
        />

        <Route
          path='/edit-user-page'
          element={
            authCtx.userIsLoggedIn ? <RestrictedAccess /> : <EditUserPage />
          }
        />

        <Route
          path='/resident'
          element={
            authCtx.userIsLoggedIn ? (
              <RestrictedAccess />
            ) : (
              <ResidentAuthPage />
            )
          }
        />

        <Route
          path='/assembleia-lista'
          element={
            authCtx.userIsLoggedIn ? (
              <ListAssemblyPage />
            ) : (
              <RestrictedAccess />
            )
          }
        />

        <Route
          path='/assembleia-lista/descricao/:id'
          element={
            authCtx.userIsLoggedIn ? (
              <DescriptionAssembly />
            ) : (
              <RestrictedAccess />
            )
          }
        />

        <Route
          path='/assembleia-edit/:id'
          element={
            authCtx.userIsLoggedIn ? (
              <AssemblyEditPage />
            ) : (
              <RestrictedAccess />
            )
          }
        />
        <Route
          path='/assembleia-lista/descricao/:id'
          element={
            authCtx.userIsLoggedIn ? (
              <DescriptionAssembly />
            ) : (
              <RestrictedAccess />
            )
          }
        />
        <Route
          path='/schedule-edit/:idAssembly/:idSchedule'
          element={
            authCtx.userIsLoggedIn ? (
              <ScheduleEditPage />
            ) : (
              <RestrictedAccess />
            )
          }
        />
        <Route
          path='/assembleia-cadastro'
          element={
            authCtx.userIsLoggedIn ? (
              <AssemblyRegister />
            ) : (
              <RestrictedAccess />
            )
          }
        />
        <Route
          path='/pauta-cadastro/:id'
          element={
            authCtx.userIsLoggedIn ? (
              <ScheduleRegister />
            ) : (
              <RestrictedAccess />
            )
          }
        />

        <Route
          path='/votacao/:idAssembly/:idSchedule'
          element={
            authCtx.userIsLoggedIn ? (
              <SchedulesVoteList />
            ) : (
              <RestrictedAccess />
            )
          }
        />

        <Route path='/restricted' element={<RestrictedAccess />} />
        <Route path='/*' element={<PageNotFound />} />
      </Route>
    </Routes>
  );
}
