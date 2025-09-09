import { createSlice } from "@reduxjs/toolkit";
import { fetchEvaluationThunk } from "../services/evaluationService";

const initialState = {
  riskLevel: null,
  loading: false,
  loaded: false,
  error: null,
};

const evaluationSlice = createSlice({
  name: "evaluation",
  initialState,
  reducers: {
    clearEvaluation: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch
      .addCase(fetchEvaluationThunk.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchEvaluationThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.loaded = true;
        state.riskLevel = action.payload;
      })
      .addCase(fetchEvaluationThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      });
  },
});

export const { clearEvaluation } = evaluationSlice.actions;
export default evaluationSlice.reducer;
