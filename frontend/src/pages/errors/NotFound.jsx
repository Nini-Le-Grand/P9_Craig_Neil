import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { setError } from "../../store/slices/errorSlice";
import { useLocation, Navigate } from "react-router-dom";

const NotFound = () => {
  const dispatch = useDispatch();
  const location = useLocation();

  useEffect(() => {
    dispatch(
      setError({
        status: 404,
        error: "NOT_FOUND",
        message: "La page que vous cherchez n'existe pas.",
        path: location.pathname,
        timestamp: new Date().toISOString(),
      })
    );
  }, [dispatch, location.pathname]);

  return <Navigate to="/error" replace />;
}

export default NotFound;