import { Route } from "react-router-dom";
import PatientProvider from "../components/providers/PatientProvider.jsx";
import PatientList from "../../pages/patients/PatientList.jsx";
import PatientDetails from "../../pages/patients/PatientDetails.jsx";
import PatientCreate from "../../pages/patients/PatientCreate.jsx";
import PatientUpdate from "../../pages/patients/PatientUpdate.jsx";

export const patientsRoutes = (
  <Route path="patients" >
    <Route index element={<PatientList />} />
    <Route path="create" element={<PatientCreate />} />

    <Route element={<PatientProvider />}>
      <Route path=":patientId" element={<PatientDetails />} />
      <Route path=":patientId/update" element={<PatientUpdate />} />
    </Route>

  </Route>
);
