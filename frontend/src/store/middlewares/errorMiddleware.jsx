import { setError } from "../slices/errorSlice";

const errorMiddleware = (store) => (next) => (action) => {
  if (action.type.endsWith("/rejected")) {
    const status = action.payload?.status || action.error?.status;
    if (status && status !== 400) {
      store.dispatch(setError(action.payload));
    }
  }

  return next(action);
};

export default errorMiddleware;
