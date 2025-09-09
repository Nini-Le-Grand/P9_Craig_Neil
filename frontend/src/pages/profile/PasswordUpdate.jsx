import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { updatePasswordThunk } from "../../store/services/passwordService";
import { clearFieldErrors } from "../../store/slices/passwordSlice";
import Container from "../../components/layouts/Container";
import Form from "../../components/forms/Form";
import passwordFields from "../../components/forms/datas/passwordFields";
import AlertMessage from "../../components/AlertMessage";
import Title from "../../components/Title";
import { profileUrl } from "../../router/urls";

const UpdatePassword = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const password = useSelector((state) => state.password);

  const [values, setValues] = useState({
    currentPassword: null,
    newPassword: null,
    confirmPassword: null,
  });

  useEffect(() => {
    return () => {
      dispatch(clearFieldErrors());
    };
  }, [dispatch]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setValues({ ...values, [name]: value });
    dispatch(clearFieldErrors());
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await dispatch(updatePasswordThunk(values));
    if (updatePasswordThunk.fulfilled.match(result)) {
      navigate(profileUrl, {
        state: { success: result.payload },
      });
    }
  };

  return (
    <Container>
      <Title title="Modifier le mot de passe" type="h2" />

      {password.error && <AlertMessage message={password.error} type="error" />}

      <Form
        fields={passwordFields}
        values={values}
        fieldErrors={password.fieldErrors}
        loading={password.loading}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onCancel={() => navigate(profileUrl)}
        submitLabel="Modifier"
        cancelLabel="Annuler"
      />
    </Container>
  );
};

export default UpdatePassword;
