import api from "./apiService";
import { createAsyncThunk } from "@reduxjs/toolkit";
import {
  searchUsersEndpoint,
  getUserEndpoint,
  createUserEndpoint,
  updateUserEndpoint,
  deleteUserEndpoint,
  resetUserPasswordEndpoint,
} from "../endpoints";

export const searchUsersThunk = createAsyncThunk(
  "users/search",
  async (keyword, { rejectWithValue }) => {
    try {
      const res = await api.get(searchUsersEndpoint(keyword));
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const fetchUserThunk = createAsyncThunk(
  "users/fetchOne",
  async (userId, { rejectWithValue }) => {
    try {
      const res = await api.get(getUserEndpoint(userId));
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const createUserThunk = createAsyncThunk(
  "users/create",
  async (userData, { rejectWithValue }) => {
    try {
      const res = await api.post(createUserEndpoint, userData);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const updateUserThunk = createAsyncThunk(
  "users/update",
  async (userData, { rejectWithValue }) => {
    try {
      const res = await api.put(updateUserEndpoint(userData.id), userData);
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const resetUserPasswordThunk = createAsyncThunk(
  "users/password/reset",
  async (userId, { rejectWithValue }) => {
    try {
      const res = await api.put(resetUserPasswordEndpoint(userId));
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const deleteUserThunk = createAsyncThunk(
  "users/delete",
  async (userId, { rejectWithValue }) => {
    try {
      const res = await api.delete(deleteUserEndpoint(userId));
      return res.data;
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);
