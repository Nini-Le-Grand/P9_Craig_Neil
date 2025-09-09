import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Outlet, useParams } from "react-router-dom";

import { setCurrentNote, clearCurrentNote } from "../../../store/slices/notesSlice";

const NoteProvider = () => {
  const dispatch = useDispatch();
  const { noteId } = useParams();
  const notes = useSelector((state) => state.notes.list);

  useEffect(() => {
    if (noteId) {
      dispatch(setCurrentNote(notes.find((n) => n.id === noteId)));
    } else {
      dispatch(clearCurrentNote());
    }
  }, [noteId, notes, dispatch]);

  return <Outlet />;
};

export default NoteProvider;
