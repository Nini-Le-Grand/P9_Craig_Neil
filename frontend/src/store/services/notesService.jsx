import { createAsyncThunk } from "@reduxjs/toolkit";
import api from "./apiService";
import { getNotesEndpoint, createNoteEndpoint, updateNoteEndpoint, deleteNoteEndpoint } from "../endpoints";

export const fetchNotesByPatientThunk = createAsyncThunk(
  "notes/fetchAllByPatient",
  async (patientId, { rejectWithValue }) => {
    try {
      const res = await api.get(getNotesEndpoint(patientId));
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const createNoteThunk = createAsyncThunk(
  "notes/create",
  async (noteData, { rejectWithValue }) => {
    try {
      const res = await api.post(createNoteEndpoint, noteData);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const updateNoteThunk = createAsyncThunk(
  "notes/update",
  async (noteData, { rejectWithValue }) => {
    try {
      console.log(noteData.note)
      const res = await api.put(updateNoteEndpoint(noteData.id), noteData);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const deleteNoteThunk = createAsyncThunk(
  "notes/delete",
  async (patientId, { rejectWithValue }) => {
    try {
      const res = await api.delete(deleteNoteEndpoint(patientId));
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);