import { useState } from "react";
import { useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import Container from "../../components/layouts/Container";
import Title from "../../components/Title";
import Actions from "../../components/Actions";
import List from "../../components/lists/List";
import Loading from "../../components/Loading";
import AlertMessage from "../../components/AlertMessage";
import TextInput from "../../components/forms/inputs/TextInput";
import { patientUrl, patientCreateUrl } from "../../router/urls";

const PatientList = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const patients = useSelector((state) => state.patients);
  const successMessage = location.state?.success;

  const [search, setSearch] = useState("");

  if (patients.loading)
    return <Loading message="Chargement de la liste des patients" />;

  const topActions = [
    { label: "Ajouter un patient", onClick: () => navigate(patientCreateUrl) },
  ];

  const filteredPatients = patients.list.filter((p) =>
    `${p.firstName} ${p.lastName} ${p.email}`
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  return (
    <Container>
      <Title title="Liste des patients" type="h2" />

      <Actions actions={topActions} />

      {successMessage && <AlertMessage message={successMessage} />}

      <div>
        <TextInput
          name="search"
          placeholder="Rechercher un patient"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      {filteredPatients.length === 0 ? (
        <AlertMessage message="Aucun patient trouvé" type="error" />
      ) : (
        <List
          items={filteredPatients}
          label={(p) => `${p.firstName} ${p.lastName} - ${p.email}`}
          actionLabel={() => "Voir"}
          actionOnClick={(p) => navigate(patientUrl(p.id))}
        />
      )}
    </Container>
  );
};

export default PatientList;
