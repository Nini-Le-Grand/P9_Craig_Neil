const Actions = ({ actions = [] }) => {
  return (
    <div style={styles.actions}>
      {actions.map((a, i) => (
        <button key={i} onClick={a.onClick} style={styles.button}>
          {a.label}
        </button>
      ))}
    </div>
  );
};

const styles = {
  actions: {
    display: "flex",
    gap: "10px",
    justifyContent: "center",
    marginTop: "20px",
  },
  button: {
    padding: "10px 15px",
    borderRadius: "5px",
    border: "none",
    cursor: "pointer",
    color: "#fff",
    fontWeight: "bold",
    backgroundColor: "#3498db",
  },
};

export default Actions;
