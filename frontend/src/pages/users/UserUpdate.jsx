import { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { clearCurrentErrors } from "../../store/slices/usersSlice";
import { updateUserThunk } from "../../store/services/usersService";
import userFields from "../../components/forms/datas/userFields";
import Form from "../../components/forms/Form";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";
import Container from "../../components/layouts/Container";
import { usersUrl, userUrl } from "../../router/urls";

const UserUpdate = () => {
  const user = useSelector((state) => state.users.current);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [values, setValues] = useState({
    id: user.id,
    firstName: user.firstName,
    lastName: user.lastName,
    email: user.email,
    gender: user.gender,
    dateOfBirth: user.dateOfBirth,
    address: user.address,
    phone: user.phone,
    doctorId: user.doctorId,
  });

    useEffect(() => {
    if (user?.id) {
      setValues({ ...user });
    }
  }, [user]);

  useEffect(() => {
    return () => {
      dispatch(clearCurrentErrors());
    };
  }, [dispatch]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setValues((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await dispatch(updateUserThunk(values));
    if (updateUserThunk.fulfilled.match(result)) {
      navigate(userUrl(result.payload.id), {
        state: { success: "Utilisateur mis à jour avec succès !" },
      });
    }
  };

  return (
    <Container>
      <Title title={`Modifier l'utilisateur ${user.firstName} ${user.lastName}`} type="h2" />

      {user.error && <AlertMessage message={user.error} type="error" />}

      <Form
        fields={userFields}
        values={values}
        fieldErrors={user.fieldErrors}
        loading={user.loading}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onCancel={() => navigate(usersUrl)}
        submitLabel="Modifier"
        cancelLabel="Annuler"
      />
    </Container>
  );
};

export default UserUpdate;
