import { useDispatch, useSelector } from "react-redux";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Form from "../../components/forms/Form";
import Container from "../../components/layouts/Container";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";
import { clearCurrentErrors } from "../../store/slices/notesSlice";
import { createNoteThunk } from "../../store/services/notesService";
import noteFields from "../../components/forms/datas/noteFields";
import { noteUrl, notesUrl } from "../../router/urls";

const NoteCreate = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const patient = useSelector((state) => state.patients.current);
  const note = useSelector((state) => state.notes.current)

  const [values, setValues] = useState({
    patientId: patient.id,
    note: null,
  });

  useEffect(() => {
    if (patient.id) {
      setValues({ patientId: patient.id });
    }
  }, [patient.id]);

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
    const result = await dispatch(createNoteThunk(values));
    if (createNoteThunk.fulfilled.match(result)) {
      navigate(noteUrl(patient.id, result.payload.id), {
        state: { success: "La note a été créé avec succès" },
      });
    }
  };

  return (
    <Container>
      <Title title={`Ajouter une note pour ${patient.firstName} ${patient.lastName}`} type="h2" />
      {note.error && <AlertMessage message={note.error} type="error" />}
      <Form
        fields={noteFields}
        values={values}
        fieldErrors={note.fieldErrors}
        loading={note.loading}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onCancel={() => navigate(notesUrl(patient.id))}
        submitLabel="Créer"
        cancelLabel="Annuler"
      />
    </Container>
  );
};

export default NoteCreate;
