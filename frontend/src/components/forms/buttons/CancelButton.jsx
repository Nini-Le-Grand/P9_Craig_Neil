const CancelButton = ({ onClick, label }) => (
  <button
    type="button"
    onClick={onClick}
    style={{
      padding: "10px",
      backgroundColor: "#ccc",
      color: "#333",
      border: "none",
      borderRadius: "5px",
      cursor: "pointer",
    }}
  >
    {label}
  </button>
);

export default CancelButton;
