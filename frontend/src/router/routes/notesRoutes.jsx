import { Route } from "react-router-dom";
import NoteList from "../../pages/notes/NoteList.jsx";
import NoteDetails from "../../pages/notes/NoteDetails.jsx";
import NoteCreate from "../../pages/notes/NoteCreate.jsx";
import NoteUpdate from "../../pages/notes/NoteUpdate.jsx";
import NoteProvider from "../components/providers/NoteProvider.jsx";

export const notesRoutes = (
  <Route path="patients/:patientId/notes">
    <Route index element={<NoteList />} />
    <Route path="create" element={<NoteCreate />} />

    <Route element={<NoteProvider />}>
      <Route path=":noteId" element={<NoteDetails />} />
      <Route path=":noteId/update" element={<NoteUpdate />} />
    </Route>
  </Route>
);
