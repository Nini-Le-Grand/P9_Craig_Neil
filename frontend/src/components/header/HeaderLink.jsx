import { NavLink } from "react-router-dom";

const HeaderLink = ({ to, label }) => {
  const style = ({ isActive }) =>
    isActive ? { ...styles.link, ...styles.active } : styles.link;

  return (
    <NavLink to={to} style={style}>
      {label}
    </NavLink>
  );
};

const styles = {
  link: {
    textDecoration: "none",
    color: "#333",
    fontWeight: "bold",
  },
  active: {
    color: "#007bff",
    borderBottom: "2px solid #007bff",
  },
};

export default HeaderLink;
