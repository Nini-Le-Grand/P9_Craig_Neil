import { createSlice } from "@reduxjs/toolkit";
import { loginThunk } from "../services/authService";

const initialState = {
  token: localStorage.getItem("token") || null,
  role: localStorage.getItem("role") || "",
  loading: false,
  error: null,
  fieldErrors: {},
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    clearFieldErrors(state) {
      state.fieldErrors = {};
    },
    logout(state) {
      state.token = null;
      state.role = null;
      state.loading = false;
      state.error = null;
      state.fieldErrors = {};
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginThunk.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(loginThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.token = action.payload.token;
        state.role = action.payload.role;
      })
      .addCase(loginThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload?.message;
        state.fieldErrors = action.payload?.fieldErrors;
      });
  },
});

export const { logout, clearFieldErrors } = authSlice.actions;
export default authSlice.reducer;
