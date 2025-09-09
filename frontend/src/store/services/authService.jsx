import { createAsyncThunk } from "@reduxjs/toolkit";
import api from "./apiService";
import { jwtDecode } from "jwt-decode";
import { logout } from "../slices/authSlice";
import { loginEndpoint } from "../endpoints";

export const loginThunk = createAsyncThunk(
  "auth/login",
  async ({ email, password }, { rejectWithValue }) => {
    try {
      const res = await api.post(loginEndpoint, { email, password });
      const token = res.data.token;
      const decoded = jwtDecode(token);
      localStorage.setItem("token", token);
      localStorage.setItem("role", decoded.role);
      return {
        token,
        role: decoded.role,
      };
    } catch (err) {
      return rejectWithValue(err.response?.data);
    }
  }
);

export const logoutThunk = () => (dispatch) => {
  localStorage.removeItem("token");
  localStorage.removeItem("role");
  dispatch(logout())
  dispatch({ type: "RESET_STORE" });
};
