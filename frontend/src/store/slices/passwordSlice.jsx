import { createSlice } from "@reduxjs/toolkit";
import { updatePasswordThunk } from "../services/passwordService";

const initialState = {
  loading: false,
  error: null,
  fieldErrors: {},
};

const passwordSlice = createSlice({
  name: "password",
  initialState,
  reducers: {
    clearFieldErrors(state) {
      state.fieldErrors = {};
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(updatePasswordThunk.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.fieldErrors = {};
      })
      .addCase(updatePasswordThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.error = null;
        state.fieldErrors = {};
      })
      .addCase(updatePasswordThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload?.message;
        state.fieldErrors = action.payload?.fieldErrors;
      });
  },
});

export const { clearFieldErrors } = passwordSlice.actions;
export default passwordSlice.reducer;
