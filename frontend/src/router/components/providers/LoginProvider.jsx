import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { Outlet } from "react-router-dom";
import { resetStore } from "../../../store/store";

const LoginProvider = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    localStorage.removeItem("token")
    localStorage.removeItem("role")
    dispatch(resetStore());
  }, [dispatch]);

  return <Outlet />;
};

export default LoginProvider;
