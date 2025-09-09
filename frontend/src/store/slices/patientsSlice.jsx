import { createSlice } from "@reduxjs/toolkit";
import {
  fetchPatientsThunk,
  fetchPatientThunk,
  createPatientThunk,
  updatePatientThunk,
  deletePatientThunk,
} from "../services/patientsService";

const currentInitialState = {
  id: null,
  firstName: null,
  lastName: null,
  email: null,
  gender: null,
  dateOfBirth: null,
  address: null,
  phone: null,
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

const patientsSlice = createSlice({
  name: "patients",
  initialState,
  reducers: {
    clearCurrentErrors: (state) => {
      state.current.error = null;
      state.current.fieldErrors = {};
    },
    clearCurrentPatient: (state) => {
        state.current = currentInitialState;
    }
  },
  extraReducers: (builder) => {
    builder
      // Fetch all
      .addCase(fetchPatientsThunk.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPatientsThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.loaded = true;
        state.list = action.payload;
      })
      .addCase(fetchPatientsThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      })

      // Fetch one
      .addCase(fetchPatientThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(fetchPatientThunk.fulfilled, (state, action) => {
        state.current.loading = false;
        state.current.loaded = true;
        state.current.error = null;
        state.current.fieldErrors = {};
        Object.assign(state.current, action.payload);
      })
      .addCase(fetchPatientThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
        state.current.fieldErrors = action.payload?.fieldErrors;
      })

      // Create
      .addCase(createPatientThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(createPatientThunk.fulfilled, (state, action) => {
        state.current.loading = false;
        state.current.loaded = true;
        state.current.error = null;
        state.current.fieldErrors = {};
        Object.assign(state.current, action.payload);
        state.list.push(action.payload);
      })
      .addCase(createPatientThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
        state.current.fieldErrors = action.payload?.fieldErrors;
      })

      // Update
      .addCase(updatePatientThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(updatePatientThunk.fulfilled, (state, action) => {
        state.current.loading = false;
        state.current.loaded = true;
        state.current.error = null;
        state.current.fieldErrors = {};
        Object.assign(state.current, action.payload);
        state.list = state.list.map((p) =>
          p.id === action.payload.id ? action.payload : p
        );
      })
      .addCase(updatePatientThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
        state.current.fieldErrors = action.payload?.fieldErrors;
      })

      // Delete
      .addCase(deletePatientThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(deletePatientThunk.fulfilled, (state, action) => {
        state.current = currentInitialState;
        state.list = state.list.filter((p) => p.id !== action.meta.arg);
      })
      .addCase(deletePatientThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
      }) 
  },
});

export const { clearCurrentErrors, clearCurrentPatient } = patientsSlice.actions;
export default patientsSlice.reducer;
