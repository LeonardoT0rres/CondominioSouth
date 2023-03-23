import { Fragment } from "react";
import { Outlet } from "react-router-dom";
import { MainHeader } from "./MainHeader";

export function Layout() {
  return (
    <Fragment>
      <MainHeader />
      <Outlet />
    </Fragment>
  )
}
