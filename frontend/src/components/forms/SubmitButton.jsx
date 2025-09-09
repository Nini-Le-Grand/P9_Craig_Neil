const SubmitButton = ({ label, disabled }) => (
  <button
    type="submit"
    disabled={disabled}
    style={{
      padding: "10px",
      backgroundColor: "#3498db",
      color: "#fff",
      border: "none",
      borderRadius: "5px",
      cursor: disabled ? "not-allowed" : "pointer",
    }}
  >
    {label}
  </button>
);

export default SubmitButton;
