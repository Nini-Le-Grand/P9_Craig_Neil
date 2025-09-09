import { createSlice } from "@reduxjs/toolkit";
import {
  fetchNotesByPatientThunk,
  createNoteThunk,
  updateNoteThunk,
  deleteNoteThunk,
} from "../services/notesService";

const currentInitialState = {
  id: null,
  patientId: null,
  dateTime: null,
  note: null,
  loading: false,
  loaded: false,
  error: null,
  fieldErrors: {},
};

const initialState = {
  list: [],
  current: currentInitialState,
  loading: false,
  loaded: false,
  error: null,
};

const notesSlice = createSlice({
  name: "notes",
  initialState,
  reducers: {
    setCurrentNote: (state, action) => {
      state.current = {
        ...currentInitialState,
        ...action.payload,
        loaded: true,
      };
    },
    clearCurrentErrors: (state) => {
      state.current.error = null;
      state.current.fieldErrors = {};
    },
    clearNotes: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder
      // fetchAllByPatient
      .addCase(fetchNotesByPatientThunk.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchNotesByPatientThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.loaded = true;
        state.list = action.payload;
      })
      .addCase(fetchNotesByPatientThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload?.message;
      })

      // Create
      .addCase(createNoteThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(createNoteThunk.fulfilled, (state, action) => {
        state.current.loading = false;
        state.current.loaded = true;
        state.current.error = null;
        state.current.fieldErrors = {};
        Object.assign(state.current, action.payload);
        state.list.push(action.payload);
      })
      .addCase(createNoteThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
        state.current.fieldErrors = action.payload?.fieldErrors;
      })

      // Update
      .addCase(updateNoteThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(updateNoteThunk.fulfilled, (state, action) => {
        state.current.loading = false;
        state.current.loaded = true;
        state.current.error = null;
        state.current.fieldErrors = {};
        Object.assign(state.current, action.payload);
        state.list = state.list.map((p) =>
          p.id === action.payload.id ? action.payload : p
        );
      })
      .addCase(updateNoteThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
        state.current.fieldErrors = action.payload?.fieldErrors;
      })

      // Delete
      .addCase(deleteNoteThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(deleteNoteThunk.fulfilled, (state, action) => {
        state.current = currentInitialState;
        state.list = state.list.filter((p) => p.id !== action.meta.arg);
      })
      .addCase(deleteNoteThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
      });
  },
});

export const { setCurrentNote, clearCurrentErrors, clearNotes, clearCurrentNote } =
  notesSlice.actions;
export default notesSlice.reducer;
