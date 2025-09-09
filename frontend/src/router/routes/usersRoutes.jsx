import { Route } from "react-router-dom";
import UserList from "../../pages/users/UserList.jsx";
import UserDetails from "../../pages/users/UserDetails.jsx";
import UserCreate from "../../pages/users/UserCreate.jsx";
import UserUpdate from "../../pages/users/UserUpdate.jsx";

export const usersRoutes = (
  <Route path="users" >
    <Route index element={<UserList />} />
    <Route path="create" element={<UserCreate />} />
    <Route path=":userId" element={<UserDetails />} />
    <Route path=":userId/update" element={<UserUpdate />} />
  </Route>
);
