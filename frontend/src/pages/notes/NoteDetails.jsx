import Container from "../../components/layouts/Container";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { deleteNoteThunk } from "../../store/services/notesService";
import Actions from "../../components/Actions";
import NoteContent from "../../components/NoteContent";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";
import EvaluationIndicator from "../../components/EvaluationIndicator";
import { notesUrl, noteUpdateUrl } from "../../router/urls";
import formatDate from "../../utils/dateFormatter";

const NoteDetails = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const patient = useSelector((state) => state.patients.current);
  const note = useSelector((state) => state.notes.current);

  const successMessage = location.state?.success;

  const handleDeleteClick = async () => {
    const result = await dispatch(deleteNoteThunk(note.id));
    if (deleteNoteThunk.fulfilled.match(result)) {
      navigate(notesUrl(patient.id), {
        state: { success: result.payload },
      });
    }
  };

  const actions = [
    {
      label: "Retour à la liste",
      onClick: () => navigate(notesUrl(patient.id)),
    },
    {
      label: "Modifier la note",
      onClick: () => navigate(noteUpdateUrl(patient.id, note.id)),
    },
    { label: "Supprimer la note", onClick: () => handleDeleteClick() },
  ];

  return (
    <Container>
      <Title
        title={`Note sur : ${patient.firstName} ${patient.lastName}`}
        type="h2"
      />
      <EvaluationIndicator />
      {successMessage && <AlertMessage message={successMessage} />}
      <p>{formatDate(note.dateTime)}</p>
      <NoteContent text={note.note} />
      <Actions actions={actions} />
    </Container>
  );
};

export default NoteDetails;
