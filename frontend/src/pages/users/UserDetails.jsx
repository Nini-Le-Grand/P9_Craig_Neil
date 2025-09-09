import { useDispatch, useSelector } from "react-redux";
import {
  resetUserPasswordThunk,
  deleteUserThunk,
} from "../../store/services/usersService";
import { useNavigate, useLocation } from "react-router-dom";
import Container from "../../components/layouts/Container";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";
import InfoCard from "../../components/infocards/InfoCard";
import userFields from "../../components/infocards/data/userFields";
import Actions from "../../components/Actions";
import Loading from "../../components/Loading";
import { usersUrl, userUrl, userUpdateUrl } from "../../router/urls";

const UserDetails = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const user = useSelector((state) => state.users.current);
  const successMessage = location.state?.success;

  if (user.loading) return <Loading message="Chargement..." />;

  const handlePasswordClick = async () => {
    const result = await dispatch(resetUserPasswordThunk(user.id));
    if (resetUserPasswordThunk.fulfilled.match(result)) {
      navigate(userUrl(user.id), {
        state: { success: result.payload },
      });
    }
  };

  const handleDeleteClick = async () => {
    const result = await dispatch(deleteUserThunk(user.id));
    if (deleteUserThunk.fulfilled.match(result)) {
      navigate(usersUrl, {
        state: { success: result.payload },
      });
    }
  };

  const topActions = [
    { label: "Retour à la liste", onClick: () => navigate(usersUrl) },
  ];

  const actions = [
    {
      label: "Modifier l'utilisateur",
      onClick: () => navigate(userUpdateUrl(user.id)),
    },
    {
      label: "Réinitialiser le mot de passe",
      onClick: () => handlePasswordClick(),
    },
    { label: "Supprimer l'utilisateur", onClick: () => handleDeleteClick() },
  ];

  const title = `${user.firstName} ${user.lastName}`;

  const fields = userFields.map((f) => ({
    label: f.label,
    value: user[f.key],
  }));

  return (
    <Container>
      <Title
        title={`Utilisateur ${user.firstName} ${user.lastName}`}
        type="h2"
      />

      {successMessage && <AlertMessage message={successMessage} />}

      <Actions actions={topActions} />
      <InfoCard title={title} fields={fields} actions={actions} />
      <Actions actions={actions} />

    </Container>
  );
};

export default UserDetails;
