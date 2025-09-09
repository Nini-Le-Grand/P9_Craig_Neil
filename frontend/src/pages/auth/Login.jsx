import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import Form from "../../components/forms/Form";
import loginForm from "../../components/forms/datas/loginFields";
import { loginThunk, logoutThunk } from "../../store/services/authService";
import { logout } from "../../store/slices/authSlice";
import { clearError } from "../../store/slices/errorSlice";
import Container from "../../components/layouts/Container";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const auth = useSelector((state) => state.auth);

  const [values, setValues] = useState({
    email: "",
    password: "",
  });

  useEffect(() => {
    localStorage.clear();
    dispatch(logoutThunk());
    dispatch(logout())
  }, [dispatch]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setValues((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    dispatch(clearError())
    const result = await dispatch(loginThunk(values));
    if (loginThunk.fulfilled.match(result)) {
      navigate("/profile");
    }
  };

  return (
    <Container size="small">
      <Title title="Connexion" />

      {auth.error && <AlertMessage message={auth.error} type="error" />}

      <Form
        fields={loginForm}
        values={values}
        fieldErrors={auth.fieldErrors}
        loading={auth.loading}
        onChange={handleChange}
        onSubmit={handleSubmit}
        submitLabel="Se connecter"
      />
    </Container>
  );
};

export default Login;
