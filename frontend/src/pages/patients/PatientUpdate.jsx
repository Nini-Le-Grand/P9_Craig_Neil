import { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { clearCurrentErrors } from "../../store/slices/patientsSlice";
import { updatePatientThunk } from "../../store/services/patientsService";
import { useNavigate } from "react-router-dom";
import Form from "../../components/forms/Form";
import patientFields from "../../components/forms/datas/patientFields";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";
import Container from "../../components/layouts/Container";
import { patientUrl } from "../../router/urls";

const PatientUpdate = () => {
  const patient = useSelector((state) => state.patients.current);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [values, setValues] = useState({
    id: patient.id,
    firstName: patient.firstName,
    lastName: patient.lastName,
    email: patient.email,
    gender: patient.gender,
    dateOfBirth: patient.dateOfBirth,
    address: patient.address,
    phone: patient.phone,
    doctorId: patient.doctorId,
  });

  useEffect(() => {
    if (patient?.id) {
      setValues({ ...patient });
    }
  }, [patient]);

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
    const result = await dispatch(updatePatientThunk(values));
    if (updatePatientThunk.fulfilled.match(result)) {
      navigate(patientUrl(result.payload.id), {
        state: { success: "Patient mis à jour avec succès !" },
      });
    }
  };

  return (
    <Container>
      <Title
        title={`Modifier le patient : ${patient.firstName} ${patient.lastName}`}
        type="h2"
      />

      {patient.error && <AlertMessage message={patient.error} type="error" />}

      <Form
        fields={patientFields}
        values={values}
        fieldErrors={patient.fieldErrors}
        loading={patient.loading}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onCancel={() => navigate(patientUrl(patient.id))}
        submitLabel="Modifier"
        cancelLabel="Annuler"
      />
    </Container>
  );
};

export default PatientUpdate;
