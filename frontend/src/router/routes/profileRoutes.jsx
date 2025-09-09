import { Route } from "react-router-dom";
import ProfileDetails from "../../pages/profile/ProfileDetails.jsx";
import ProfileUpdate from "../../pages/profile/ProfileUpdate.jsx";
import PasswordUpdate from "../../pages/profile/PasswordUpdate.jsx";

export const profileRoutes = (
  <Route path="profile" >
    <Route index element={<ProfileDetails />} />
    <Route path="update" element={<ProfileUpdate />} />
    <Route path="password" element={<PasswordUpdate />} />
  </Route>
);
