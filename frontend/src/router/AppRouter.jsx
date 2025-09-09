import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { profileRoutes } from "./routes/profileRoutes.jsx";
import { patientsRoutes } from "./routes/patientsRoutes.jsx";
import { usersRoutes } from "./routes/usersRoutes.jsx";
import { notesRoutes } from "./routes/notesRoutes.jsx";
import Login from "../pages/auth/Login.jsx";
import GlobalLayout from "../components/layouts/GlobalLayout.jsx";
import RequireAuth from "./components/requirements/RequireAuth.jsx";
import RequireRole from "./components/requirements/RequireRole.jsx";
import ErrorPage from "../pages/errors/ErrorPage.jsx";
import NotFound from "../pages/errors/NotFound.jsx";
import LoginProvider from "./components/providers/LoginProvider.jsx";
import ConnectedUserProvider from "./components/providers/ConnectedUserProvider.jsx";
import PatientProvider from "./components/providers/PatientProvider.jsx";
import UserProvider from "./components/providers/UserProvider.jsx";
import ErrorWatcher from "./components/watchers/ErrorWatcher.jsx";

function AppRouter() {
  return (
    <Router>
      <Routes>
        <Route element={<LoginProvider />}>
          <Route path="/" element={<Login />} />
          <Route path="/login" element={<Login />} />
        </Route>

        <Route element={<RequireAuth />}>
          <Route element={<ErrorWatcher />}>
            <Route element={<ConnectedUserProvider />}>
              <Route element={<PatientProvider />}>
                <Route element={<GlobalLayout />}>
                  {profileRoutes}

                  <Route element={<RequireRole allowedRoles="ROLE_USER" />}>
                    {patientsRoutes}
                    {notesRoutes}
                  </Route>

                  <Route element={<RequireRole allowedRoles="ROLE_ADMIN" />}>
                    <Route element={<UserProvider />}>{usersRoutes}</Route>
                  </Route>
                </Route>
              </Route>
            </Route>
          </Route>
        </Route>
        <Route path="/error" element={<ErrorPage />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}

export default AppRouter;
