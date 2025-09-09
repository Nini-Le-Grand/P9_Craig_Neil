import { useDispatch, useSelector } from "react-redux";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { clearCurrentErrors } from "../../store/slices/usersSlice";
import { createUserThunk } from "../../store/services/usersService";
import userFields from "../../components/forms/datas/userFields";
import Form from "../../components/forms/Form";
import Container from "../../components/layouts/Container";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";
import { usersUrl, userUrl } from "../../router/urls";

const UserCreate = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const user = useSelector((state) => state.users.current);

  const [values, setValues] = useState({
    firstName: null,
    lastName: null,
    email: null,
    phone: null,
    dateOfBirth: null,
    gender: null,
    address: null,
  });

  useEffect(() => {
    return () => {
      dispatch(clearCurrentErrors());
    };
  }, [dispatch]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setValues({ ...values, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await dispatch(createUserThunk(values));
    if (createUserThunk.fulfilled.match(result)) {
      navigate(userUrl(result.payload.id), {
        state: { success: "L'utilisateur a été créé avec succès" },
      });
    }
  };

  return (
    <Container>
      <Title title="Créer un utilisateur" type="h2" />
      {user.error && <AlertMessage message={user.error} type="error" />}
      <Form
        fields={userFields}
        values={values}
        fieldErrors={user.fieldErrors}
        loading={user.loading}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onCancel={() => navigate(usersUrl)}
        submitLabel="Créer"
        cancelLabel="Annuler"
      />
    </Container>
  );
};

export default UserCreate;
