import { useSelector } from "react-redux";
import { useNavigate, useLocation } from "react-router-dom";
import List from "../../components/lists/List";
import Title from "../../components/Title";
import Container from "../../components/layouts/Container";
import Actions from "../../components/Actions";
import AlertMessage from "../../components/AlertMessage";
import formatDate from "../../utils/dateFormatter";
import { noteUrl, noteCreateUrl, patientUrl } from "../../router/urls";
import EvaluationIndicator from "../../components/EvaluationIndicator";

const NoteList = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const patient = useSelector((state) => state.patients.current);
  const notes = useSelector((state) => state.notes);

  const successMessage = location.state?.success;

  const actions = [
    {
      label: "Revenir à la fiche patient",
      onClick: () => navigate(patientUrl(patient.id)),
    },
    {
      label: "Ajouter une note",
      onClick: () => navigate(noteCreateUrl(patient.id)),
    },
  ];

  const sortedList = notes.list
    .slice()
    .sort((a, b) => new Date(b.dateTime) - new Date(a.dateTime));

  return (
    <Container>
      <Title
        title={`Notes de ${patient.firstName} ${patient.lastName}`}
        type="h2"
      />
      <EvaluationIndicator />

      {successMessage && <AlertMessage message={successMessage} />}

      {notes.list.length === 0 && <p>Ce patient n'a pas de note</p>}

      <Actions actions={actions} />
      <List
        items={sortedList}
        label={(n) => formatDate(n.dateTime)}
        actionLabel={() => "Voir"}
        actionOnClick={(n) => navigate(noteUrl(patient.id, n.id))}
      />
    </Container>
  );
};

export default NoteList;
