import { useSelector } from "react-redux";
import { useNavigate, useLocation } from "react-router-dom";
import InfoCard from "../../components/infocards/InfoCard";
import profileFields from "../../components/infocards/data/profileFields";
import AlertMessage from "../../components/AlertMessage";
import Loading from "../../components/Loading";
import Title from "../../components/Title";
import Container from "../../components/layouts/Container";
import Actions from "../../components/Actions";
import { profileUpdateUrl, passwordUpdateUrl } from "../../router/urls";

const ProfileDetails = () => {
  const navigate = useNavigate();

  const profile = useSelector((state) => state.profile);
  const location = useLocation();
  const successMessage = location.state?.success;

  if (profile.loading) return <Loading message="Chargement du profil..." />

  const actions = [
    { label: "Modifier le profil", onClick: () => navigate(profileUpdateUrl) },
    { label: "Modifier le mot de passe", onClick: () => navigate(passwordUpdateUrl) },
  ];

  const fields = profileFields.map((f) => ({
    label: f.label,
    value: profile[f.key] || "-",
  }));

  return (
    <Container>
      <Title title="Mes informations :" type="h2" />
      {successMessage && <AlertMessage message={successMessage}/>}
      <InfoCard fields={fields} actions={actions} />
      <Actions actions={actions} />
    </Container>
  );
};

export default ProfileDetails;