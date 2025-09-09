import { useDispatch, useSelector } from "react-redux";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createPatientThunk } from "../../store/services/patientsService";
import { clearCurrentErrors } from "../../store/slices/patientsSlice";
import Form from "../../components/forms/Form";
import patientFields from "../../components/forms/datas/patientFields";
import Container from "../../components/layouts/Container";
import Title from "../../components/Title";
import AlertMessage from "../../components/AlertMessage";
import { patientUrl, patientsUrl } from "../../router/urls";

const PatientCreate = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const patient = useSelector((state) => state.patients.current);
  const userId = useSelector((state) => state.profile.id);

  const [values, setValues] = useState({
    firstName: null,
    lastName: null,
    email: null,
    phone: null,
    dateOfBirth: null,
    gender: null,
    address: null,
    doctorId: userId,
  });

    useEffect(() => {
    if (userId) {
      setValues({ doctorId: userId });
    }
  }, [userId]);

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
    const result = await dispatch(createPatientThunk(values));
    if (createPatientThunk.fulfilled.match(result)) {
      navigate(patientUrl(result.payload.id), {
        state: { success: "Le patient a été créé avec succès" },
      });
    }
  };

  return (
    <Container>
      <Title title="Créer un patient" type="h2" />
      {patient.error && <AlertMessage message={patient.error} type="error" />}
      <Form
        fields={patientFields}
        values={values}
        fieldErrors={patient.fieldErrors}
        loading={patient.loading}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onCancel={() => navigate(patientsUrl)}
        submitLabel="Créer"
        cancelLabel="Annuler"
      />
    </Container>
  );
};

export default PatientCreate;
