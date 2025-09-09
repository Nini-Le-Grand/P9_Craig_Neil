const AlertMessage = ({ message, type = "success" }) => {
  if (!message) return null;

  const styles = {
    container: {
      padding: "10px 15px",
      borderRadius: "5px",
      marginBottom: "15px",
      fontWeight: "bold",
      color: type === "success" ? "#155724" : "#721c24",
      backgroundColor: type === "success" ? "#d4edda" : "#f8d7da",
      border: type === "success" ? "1px solid #c3e6cb" : "1px solid #f5c6cb",
    },
  };

  return <div style={styles.container}>{message}</div>;
};

export default AlertMessage;
