import { useSelector } from "react-redux";
import HeaderLink from "./HeaderLink";
import LogoutButton from "./LogoutButton";

const navLinks = [
  { to: "/profile", label: "Profil", roles: ["USER", "ADMIN"] },
  { to: "/patients", label: "Mes patients", roles: ["USER"] },
  { to: "/users", label: "Utilisateurs", roles: ["ADMIN"] },
];

const Header = () => {
  const role = useSelector((state) => state.auth.role);

  return (
    <header style={styles.header}>
      <nav style={styles.nav}>
        <div style={styles.links}>
          {navLinks
            .filter((link) => link.roles.includes(role))
            .map((link) => (
              <HeaderLink key={link.to} to={link.to} label={link.label} />
            ))}
        </div>

        <LogoutButton />
      </nav>
    </header>
  );
};

const styles = {
  header: {
    backgroundColor: "#f5f5f5",
    padding: "10px 20px",
    borderBottom: "1px solid #ddd",
  },
  nav: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  links: {
    display: "flex",
    alignItems: "center",
    gap: "20px",
  },
};

export default Header;
