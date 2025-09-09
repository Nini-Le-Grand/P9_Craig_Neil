import { createAsyncThunk } from "@reduxjs/toolkit";
import api from "./apiService";
import { updatePasswordEndpoint } from "../endpoints";

export const updatePasswordThunk = createAsyncThunk(
  "password/update",
  async (passwordData, { rejectWithValue }) => {
    try {
      const res = await api.put(updatePasswordEndpoint, passwordData);
      return res.data;
    } catch (err) {
      return rejectWithValue(
        err.response?.data || "Erreur lors de la mise à jour du mot de passe"
      );
    }
  }
);
