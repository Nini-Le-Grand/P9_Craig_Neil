import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { logout } from "../../store/slices/authSlice";
import { loginUrl } from "../../router/urls";
import formatDate from "../../utils/dateFormatter";

const ErrorPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const error = useSelector((state) => state.error);

  useEffect(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    dispatch(logout());
  }, [dispatch]);

  const handleReconnect = () => {
    navigate(loginUrl);
  };

  return (
    <div style={styles.container}>
      <h1>⚠️ Erreur {error.status || "Inconnue"}</h1>
      {error && (
        <p>
          <strong>Type :</strong> {error.error}
        </p>
      )}
      {error.message && (
        <p>
          <strong>Message :</strong> {error.message}
        </p>
      )}
      {error.path && (
        <p>
          <strong>Chemin :</strong> {error.path}
        </p>
      )}
      {error.timestamp && (
        <p>
          <strong>Date :</strong> {formatDate(error.timestamp)}
        </p>
      )}

      <button onClick={handleReconnect} style={styles.button}>
        🔄 Se reconnecter
      </button>
    </div>
  );
};

const styles = {
  container: {
    textAlign: "center",
    padding: "20px",
    background: "#f8d7da",
    minHeight: "100vh",
  },
  button: {
    padding: "10px 20px",
    background: "#e74c3c",
    color: "#fff",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
  },
};

export default ErrorPage;
