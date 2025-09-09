const InfoCardField = ({ label, value }) => (
  <div style={styles.field}>
    <span style={styles.label}>{label}:</span>
    <span style={styles.value}>{value || "-"}</span>
  </div>
);

const styles = {
  field: {
    display: "flex",
    justifyContent: "space-between",
    padding: "8px 0",
    borderBottom: "1px solid #eee",
  },
  label: {
    fontWeight: "bold",
    color: "#555",
  },
  value: {
    color: "#333",
  },
};

export default InfoCardField;
