import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Outlet } from "react-router-dom";

import { fetchProfileThunk } from "../../../store/services/profileService";
import { fetchPatientsThunk } from "../../../store/services/patientsService";

const ConnectedUserProvider = () => {
  const dispatch = useDispatch();
  const auth = useSelector((state) => state.auth);

  useEffect(() => {
    if (auth.token && auth.role) {
      dispatch(fetchProfileThunk());

      if (auth.role === "USER") {
        dispatch(fetchPatientsThunk());
      }
    }
  }, [auth.token, auth.role, dispatch]);

  return <Outlet />;
};

export default ConnectedUserProvider;
