import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { searchUsersThunk } from "../../store/services/usersService";
import Container from "../../components/layouts/Container";
import Title from "../../components/Title";
import Actions from "../../components/Actions";
import List from "../../components/lists/List";
import Loading from "../../components/Loading";
import AlertMessage from "../../components/AlertMessage";
import Search from "../../components/Search";
import { userUrl, userCreateUrl } from "../../router/urls";

const UserList = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const users = useSelector((state) => state.users);

  const successMessage = location.state?.success;

  const handleSearch = (keyword) => {
    dispatch(searchUsersThunk(keyword));
  };

  const actions = [
    { label: "Créer un utilisateur", onClick: () => navigate(userCreateUrl) },
  ];

  return (
    <Container size="small">
      <Title title="Chercher un utilisateur" type="h2" />

      {successMessage && <AlertMessage message={successMessage} />}

      <Actions actions={actions} />

      <Search
        placeholder="Rechercher par nom, prénom ou email"
        onSearch={handleSearch}
      />

      {users.loading && (
        <Loading message="Chargement de la liste des utilisateurs" />
      )}
      {users.loaded && (
        <List
          items={users.list}
          label={(u) => `${u.firstName} ${u.lastName} - ${u.email}`}
          actionLabel={() => "Voir"}
          actionOnClick={(u) => navigate(userUrl(u.id))}
        />
      )}
    </Container>
  );
};

export default UserList;
