import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  status: null,
  error: null,
  message: null,
  path: null,
  timestamp: null,
};

const errorSlice = createSlice({
  name: "error",
  initialState,
  reducers: {
    setError: (state, action) => {
      state.status = action.payload.status;
      state.error = action.payload.error;
      state.message = action.payload.message;
      state.path = action.payload.path;
      state.timestamp = action.payload.timestamp;
    },
    clearError: () => initialState,
  },
});

export const { setError, clearError } = errorSlice.actions;
export default errorSlice.reducer;
