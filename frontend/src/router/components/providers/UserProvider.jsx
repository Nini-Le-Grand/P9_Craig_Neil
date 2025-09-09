import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useParams } from "react-router-dom";

import { fetchUserThunk } from "../../../store/services/usersService";
import { clearCurrentUser } from "../../../store/slices/usersSlice";

import { Outlet } from "react-router-dom";

const UserProvider = () => {
  const dispatch = useDispatch();

  const { userId } = useParams();

  useEffect(() => {
    if (userId) {
      dispatch(fetchUserThunk(userId));
    } else {
      dispatch(clearCurrentUser());
    }
  }, [userId, dispatch]);

  return <Outlet />;
};

export default UserProvider;
