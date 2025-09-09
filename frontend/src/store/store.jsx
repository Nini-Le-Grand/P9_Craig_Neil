import { configureStore, combineReducers } from "@reduxjs/toolkit";
import profileReducer from "./slices/profileSlice";
import authReducer from "./slices/authSlice";
import errorReducer from "./slices/errorSlice";
import passwordReducer from "./slices/passwordSlice";
import patientsReducer from "./slices/patientsSlice";
import usersReducer from "./slices/usersSlice";
import notesReducer from "./slices/notesSlice"
import evaluationReducer from "./slices/evaluationSlice"
import errorMiddleware from "./middlewares/errorMiddleware";

const appReducer = combineReducers({
  notes: notesReducer,
  users: usersReducer,
  patients: patientsReducer,
  evaluation: evaluationReducer,
  profile: profileReducer,
  password: passwordReducer,
  auth: authReducer,
  error: errorReducer,
});

const rootReducer = (state, action) => {
  if (action.type === "RESET_STORE") {
    state = undefined;
  }
  return appReducer(state, action);
};

const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(errorMiddleware),
});

export const resetStore = () => ({ type: "RESET_STORE" });

export default store;
