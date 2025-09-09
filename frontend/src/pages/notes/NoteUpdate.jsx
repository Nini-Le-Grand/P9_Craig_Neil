import { useDispatch, useSelector } from "react-redux";
import Container from "../../components/layouts/Container";
import Title from "../../components/Title";
import { notesUrl, noteUrl } from "../../router/urls";
import formatDate from "../../utils/dateFormatter";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { clearCurrentErrors } from "../../store/slices/notesSlice";
import { updateNoteThunk } from "../../store/services/notesService";
import AlertMessage from "../../components/AlertMessage";
import Form from "../../components/forms/Form";
import noteFields from "../../components/forms/datas/noteFields";

const NoteUpdate = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const patient = useSelector((state) => state.patients.current);
  const note = useSelector((state) => state.notes.current);

  const [values, setValues] = useState({
    id: note.id,
    patientId: note.patientId,
    note: note.note,
  });

  useEffect(() => {
    if (note.id) {
      setValues({
        id: note.id,
        patientId: note.patientId,
        note: note.note,
      });
    }
  }, [note]);

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
    console.log(values);
    const result = await dispatch(updateNoteThunk(values));
    if (updateNoteThunk.fulfilled.match(result)) {
      navigate(noteUrl(patient.id, result.payload.id), {
        state: { success: "La note a été créé avec succès" },
      });
    }
  };

  return (
    <Container>
      <Title title="Mise à jour d'une note" type="h2" />
      <p>{`${patient.firstName} ${patient.lastName}`}</p>
      <p>{formatDate(note.dateTime)}</p>
      {note.error && <AlertMessage message={note.error} type="error" />}
      <Form
        fields={noteFields}
        values={values}
        fieldErrors={note.fieldErrors}
        loading={note.loading}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onCancel={() => navigate(notesUrl(patient.id))}
        submitLabel="Modifier"
        cancelLabel="Annuler"
      />
    </Container>
  );
};

export default NoteUpdate;
