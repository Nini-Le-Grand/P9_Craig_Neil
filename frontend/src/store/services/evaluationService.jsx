import api from "./apiService";
import { createAsyncThunk } from "@reduxjs/toolkit";
import { getEvaluationEndpoint } from "../endpoints";

export const fetchEvaluationThunk = createAsyncThunk(
  "evaluation/fetch",
  async (patientId, { rejectWithValue }) => {
    try {
      const res = await api.get(getEvaluationEndpoint(patientId));
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);