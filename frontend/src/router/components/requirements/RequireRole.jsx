import { useEffect } from "react";
import { useNavigate, Outlet, useLocation } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { setError } from "../../../store/slices/errorSlice";
import { errorUrl } from "../../urls";

function RequireRole({ allowedRoles }) {
  const dispatch = useDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const role = useSelector((state) => state.auth.role);

  useEffect(() => {
    if (!allowedRoles.includes(role)) {
      dispatch(
        setError({
          status: 403,
          error: "FORBIDDEN",
          message: "Vous n'êtes pas autorisés à consulter cette page",
          path: location.pathname,
          timestamp: new Date().toISOString(),
        })
      );
      navigate(errorUrl);
    }
  }, [allowedRoles, role, dispatch, navigate, location.pathname]);

  return <Outlet />;
}

export default RequireRole;
