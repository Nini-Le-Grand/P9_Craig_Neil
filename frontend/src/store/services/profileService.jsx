import { createAsyncThunk } from "@reduxjs/toolkit";
import api from "./apiService";
import { getProfileEndpoint, updateProfileEndpoint } from "../endpoints";

export const fetchProfileThunk = createAsyncThunk(
  "profile/fetchProfile",
  async (_, { rejectWithValue }) => {
    try {
      const res = await api.get(getProfileEndpoint);
      return res.data;
    } catch (err) {
      return rejectWithValue(
        err.response?.data?.message
      );
    }
  }
);

export const updateProfileThunk = createAsyncThunk(
  "profile/updateProfile",
  async (profileData, { rejectWithValue }) => {
    try {
      const res = await api.put(updateProfileEndpoint, profileData);
      return res.data;
    } catch (err) {
      return rejectWithValue(
        err.response?.data || "Erreur lors de la mise à jour du profil",
      );
    }
  }
);
