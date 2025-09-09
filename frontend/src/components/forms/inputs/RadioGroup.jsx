const RadioGroup = ({ name, label, options, value, onChange, error }) => (
  <div style={styles.container}>
    <label style={styles.label}>{label}</label>
    {error && <p style={styles.error}>{error}</p>}
    <div style={styles.options}>
      {options.map((opt) => (
        <label key={opt.value} style={styles.option}>
          <input
            type="radio"
            name={name}
            value={opt.value}
            checked={value === opt.value}
            onChange={onChange}
          />
          {opt.label}
        </label>
      ))}
    </div>
  </div>
);

const styles = {
  container: { display: "flex", flexDirection: "column" },
  label: { fontWeight: "bold", marginBottom: "4px" },
  options: { display: "flex", gap: "10px" },
  option: { display: "flex", alignItems: "center", gap: "5px" },
  error: { color: "red", margin: "4px" },
};

export default RadioGroup;
