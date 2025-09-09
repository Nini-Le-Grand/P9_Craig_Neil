import { Navigate, Outlet } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

const RequireAuth = () => {
  const token = localStorage.getItem("token")

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  try {
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000;

    if (decoded.exp && decoded.exp < currentTime) {
      return <Navigate to="/login" replace />;
    }

    return <Outlet />;
  } catch (error) {
    return <Navigate to="/login" replace />;
  }
};

export default RequireAuth;
