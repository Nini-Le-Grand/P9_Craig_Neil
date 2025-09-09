import { createSlice } from "@reduxjs/toolkit";
import {
  fetchUserThunk,
  searchUsersThunk,
  createUserThunk,
  updateUserThunk,
  resetUserPasswordThunk,
  deleteUserThunk,
} from "../services/usersService";

const currentInitialState = {
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

const initialState = {
  list: [],
  current: currentInitialState,
  loading: false,
  loaded: false,
  error: null,
};

const usersSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    clearCurrentErrors: (state) => {
      state.current.error = null;
      state.current.fieldErrors = {};
    },
    clearCurrentUser: (state) => {
      state.current = currentInitialState;
    },
  },
  extraReducers: (builder) => {
    builder
      // Search
      .addCase(searchUsersThunk.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(searchUsersThunk.fulfilled, (state, action) => {
        state.loading = false;
        state.loaded = true;
        state.list = action.payload;
      })
      .addCase(searchUsersThunk.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      })

      // Fetch one
      .addCase(fetchUserThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(fetchUserThunk.fulfilled, (state, action) => {
        state.current.loading = false;
        state.current.loaded = true;
        state.current.error = null;
        Object.assign(state.current, action.payload);
      })
      .addCase(fetchUserThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
        state.current.fieldErrors = action.payload?.fieldErrors;
      })

      // Create
      .addCase(createUserThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(createUserThunk.fulfilled, (state, action) => {
        state.current.loading = false;
        state.current.loaded = true;
        state.current.error = null;
        state.current.fieldErrors = {};
        Object.assign(state.current, action.payload);
        state.list.push(action.payload);
      })
      .addCase(createUserThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
        state.current.fieldErrors = action.payload?.fieldErrors;
      })

      // Update
      .addCase(updateUserThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(updateUserThunk.fulfilled, (state, action) => {
        state.current.loading = false;
        state.current.loaded = true;
        state.current.error = null;
        state.current.fieldErrors = {};
        Object.assign(state.current, action.payload);
        state.list = state.list.map((p) =>
          p.id === action.payload.id ? action.payload : p
        );
      })
      .addCase(updateUserThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
        state.current.fieldErrors = action.payload?.fieldErrors;
      })

      // Reset password
      .addCase(resetUserPasswordThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(resetUserPasswordThunk.fulfilled, (state, action) => {
        state.current.loading = false;
        state.current.loaded = true;
        state.current.error = null;
      })
      .addCase(resetUserPasswordThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
      })

      // Delete
      .addCase(deleteUserThunk.pending, (state) => {
        state.current.loading = true;
        state.current.error = null;
      })
      .addCase(deleteUserThunk.fulfilled, (state, action) => {
        state.current = currentInitialState;
        state.list = state.list.filter((p) => p.id !== action.meta.arg);
      })
      .addCase(deleteUserThunk.rejected, (state, action) => {
        state.current.loading = false;
        state.current.error = action.payload?.message;
      });
  },
});

export const { clearCurrentErrors, clearCurrentUser } = usersSlice.actions;
export default usersSlice.reducer;
