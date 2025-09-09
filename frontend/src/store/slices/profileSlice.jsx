import { createSlice } from "@reduxjs/toolkit";
import {
  fetchProfileThunk,
  updateProfileThunk,
} from "../services/profileService";

const initialState = {
  id: null,
  firstName: null,
  lastName: null,
  email: null,
  gender: null,
  dateOfBirth: null,
  address: null,
  phone: null,
  role: null,
  loading: false,
  loaded: false,
  error: null,
  fieldErrors: {},
};

const profileSlice = createSlice({
  name: "profile",
  initialState,
  reducers: {
    clearErrors: (state) => {
      state.error = null;
      state.fieldErrors = {};
    },
  },
  extraReducers: (builder) => {
    builder
      // Get profile
      .addCase(fetchProfileThunk.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProfileThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.loaded = true;
        state.error = null;
        Object.assign(state, action.payload);
      })
      .addCase(fetchProfileThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })

      // Update profile
      .addCase(updateProfileThunk.pending, (state) => {
        state.loading = true;
        state.fieldErrors = {};
        state.error = null;
      })
      .addCase(updateProfileThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.error = null;
        state.fieldErrors = {};
        Object.assign(state, action.payload);
      })
      .addCase(updateProfileThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload?.message;
        state.fieldErrors = action.payload?.fieldErrors;
      });
  },
});

export const { clearErrors } = profileSlice.actions;
export default profileSlice.reducer;
