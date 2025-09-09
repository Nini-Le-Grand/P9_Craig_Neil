import api from "./apiService";
import { createAsyncThunk } from "@reduxjs/toolkit";
import {
  getPatientsEndpoint,
  getPatientEndpoint,
  createPatientEndpoint,
  updatePatientEndpoint,
  deletePatientEndpoint,
} from "../endpoints";

export const fetchPatientsThunk = createAsyncThunk(
  "patients/fetchAll",
  async (_, { rejectWithValue }) => {
    try {
      const res = await api.get(getPatientsEndpoint);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const fetchPatientThunk = createAsyncThunk(
  "patients/fetchOne",
  async (patientId, { rejectWithValue }) => {
    try {
      const res = await api.get(getPatientEndpoint(patientId));
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const createPatientThunk = createAsyncThunk(
  "patients/create",
  async (patientData, { rejectWithValue }) => {
    try {
      const res = await api.post(createPatientEndpoint, patientData);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const updatePatientThunk = createAsyncThunk(
  "patients/update",
  async (patientData, { rejectWithValue }) => {
    try {
      const res = await api.put(updatePatientEndpoint(patientData.id), patientData);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const deletePatientThunk = createAsyncThunk(
  "patients/delete",
  async (patientId, { rejectWithValue }) => {
    try {
      const res = await api.delete(deletePatientEndpoint(patientId));
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);
