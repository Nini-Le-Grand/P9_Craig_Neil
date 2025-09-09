import { useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { errorUrl } from "../../urls";
import { Outlet } from "react-router-dom";

export default function ErrorWatcher() {
  const error = useSelector((state) => state.error);
  const navigate = useNavigate();

  useEffect(() => {
    if (error?.status && error.status !== 400) {
      navigate(errorUrl);
    }
  }, [error, navigate]);

  return <Outlet />;
}
