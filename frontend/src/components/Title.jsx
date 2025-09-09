const Title = ({ title, type = "h1" }) => {
  if (type === "h1") return <h1 style={styles.h1}>{title}</h1>;
  if (type === "h2") return <h2 style={styles.h2}>{title}</h2>;
};

const styles = {
  h1: {
    fontSize: "24px",
    fontWeight: "bold",
    marginBottom: "20px",
    color: "#333",
    textAlign: "center",
    textTransform: "uppercase",
  },
  h2: {
    fontSize: "20px",
    fontWeight: "bold",
    marginBottom: "20px",
    color: "#333",
    textAlign: "center",
  },
};

export default Title;
