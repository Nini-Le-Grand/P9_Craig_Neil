const Loading = ({ message = "Chargement…" }) => {
  const styles = {
    container: {
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      minHeight: "100px",
      fontSize: "1.2rem",
      color: "#666",
    },
  };

  return <div style={styles.container}>{message}</div>;
};

export default Loading;
