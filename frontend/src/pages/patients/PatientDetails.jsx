import { useDispatch, useSelector } from "react-redux";
import { deletePatientThunk } from "../../store/services/patientsService";
import { useNavigate, useLocation } from "react-router-dom";
import Container from "../../components/layouts/Container";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";
import InfoCard from "../../components/infocards/InfoCard";
import patientFields from "../../components/infocards/data/patientFields";
import Actions from "../../components/Actions";
import Loading from "../../components/Loading";
import { patientsUrl, patientUpdateUrl, notesUrl } from "../../router/urls";
import EvaluationIndicator from "../../components/EvaluationIndicator";

const PatientDetails = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const patient = useSelector((state) => state.patients.current);

  const successMessage = location.state?.success;

  if (patient.loading) return <Loading message="Chargement..." />;

  const title = `${patient.firstName} ${patient.lastName}`;

  const fields = patientFields.map((f) => ({
    label: f.label,
    value: patient[f.key] ?? "-",
  }));

  const handleDeleteClick = async () => {
    const result = await dispatch(deletePatientThunk(patient.id));
    if (deletePatientThunk.fulfilled.match(result)) {
      navigate(patientsUrl, {
        state: { success: result.payload },
      });
    }
  };

  const topActions = [
    { label: "Retour à la liste des patients", onClick: () => navigate(patientsUrl) },
    {
      label: "Voir les notes",
      onClick: () => navigate(notesUrl(patient.id)),
    },
  ];

  const actions = [
    {
      label: "Modifier le patient",
      onClick: () => navigate(patientUpdateUrl(patient.id)),
    },
    { label: "Supprimer le patient", onClick: () => handleDeleteClick() },
  ];

  return (
    <Container>
      <Title
        title={`Patient : ${patient.firstName} ${patient.lastName}`}
        type="h2"
      />
      {successMessage && <AlertMessage message={successMessage} />}
      <div><Actions actions={topActions} /></div>
      <EvaluationIndicator />
      <InfoCard title={title} fields={fields} actions={actions} />
      <Actions actions={actions} />
    </Container>
  );
};

export default PatientDetails;
