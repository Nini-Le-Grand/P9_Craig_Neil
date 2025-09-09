const TextAreaInput = ({ name, label, value, onChange, error }) => (
  <div style={styles.container}>
    <label style={styles.label}>{label}</label>
    {error && <p style={styles.error}>{error}</p>}
    <textarea
      name={name}
      value={value}
      onChange={onChange}
      style={{ ...styles.textarea, borderColor: error ? "red" : "#ccc" }}
    />
  </div>
);

const styles = {
  container: { display: "flex", flexDirection: "column" },
  label: { fontWeight: "bold", marginBottom: "4px" },
  textarea: {
    padding: "8px",
    borderRadius: "4px",
    border: "1px solid #ccc",
    minHeight: "80px",
  },
  error: { color: "red", margin: "4px" },
};

export default TextAreaInput;
