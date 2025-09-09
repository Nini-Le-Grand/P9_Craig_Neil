import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { Outlet, useParams } from "react-router-dom";

import { fetchPatientThunk } from "../../../store/services/patientsService";
import { fetchNotesByPatientThunk } from "../../../store/services/notesService";
import { fetchEvaluationThunk } from "../../../store/services/evaluationService";
import { clearCurrentPatient } from "../../../store/slices/patientsSlice";
import { clearNotes } from "../../../store/slices/notesSlice";
import { clearEvaluation } from "../../../store/slices/evaluationSlice";

const PatientProvider = () => {
  const dispatch = useDispatch();
  const { patientId } = useParams();

  useEffect(() => {
    if (patientId) {
      dispatch(fetchPatientThunk(patientId));
      dispatch(fetchNotesByPatientThunk(patientId));
      dispatch(fetchEvaluationThunk(patientId));
    } else {
      dispatch(clearCurrentPatient());
      dispatch(clearNotes());
      dispatch(clearEvaluation());
    }
  }, [patientId, dispatch]);

  return <Outlet />;
};

export default PatientProvider;
