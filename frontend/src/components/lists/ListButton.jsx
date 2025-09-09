const ListButton = ({ label, onClick, type="button" }) => {
  return (
    <button type={type} onClick={onClick}  style={styles.button}>
      {label}
    </button>
  );
};

const styles = {
  button: {
    padding: "6px 10px",
    backgroundColor: "#2ecc71",
    color: "#fff",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
    fontSize: "0.9rem",
  },
};

export default ListButton;
