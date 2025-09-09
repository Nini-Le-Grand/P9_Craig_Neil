const TextInput = ({ name, label, placeholder = "rechercher", type = "text", value, onChange, error }) => (
  <div style={styles.container}>
    <label style={styles.label}>{label}</label>
    {error && <p style={styles.error}>{error}</p>}
    <input
      type={type}
      name={name}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      style={{ ...styles.input, borderColor: error ? "red" : "#ccc" }}
    />
  </div>
);

const styles = {
  container: { display: "flex", flexDirection: "column" },
  label: { fontWeight: "bold", marginBottom: "4px" },
  input: {
    padding: "8px",
    borderRadius: "4px",
    border: "1px solid #ccc",
  },
  error: { color: "red", margin: "4px" },
};

export default TextInput;
