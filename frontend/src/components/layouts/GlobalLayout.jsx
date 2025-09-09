import { Outlet } from "react-router-dom";
import Header from "../header/Header";

const GlobalLayout = () => {
  return (
    <div style={styles.container}>
      <Header />
      <main style={styles.main}>
        <Outlet />
      </main>
    </div>
  );
};

const styles = {
  container: {
    display: "flex",
    flexDirection: "column",
    minHeight: "100vh",
    boxSizing: "border-box",
    backgroundColor: "#f4f6f8",
  },
  main: {
    flex: 1,
    padding: "20px",
    maxWidth: "1200px",
    width: "100%",
    margin: "0 auto",
  },
};

export default GlobalLayout;
