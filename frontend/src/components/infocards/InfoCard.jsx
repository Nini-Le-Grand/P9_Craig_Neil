import InfoCardField from "./InfoCardField";

const InfoCard = ({ fields = [], actions = [] }) => {
  return (
    <div style={styles.card}>
      {fields.map((f, i) => (
        <InfoCardField key={i} label={f.label} value={f.value} />
      ))}
    </div>
  );
};

const styles = {
  card: {
    maxWidth: "600px",
    margin: "20px auto",
    padding: "20px",
    borderRadius: "8px",
    backgroundColor: "#fff",
  },
};

export default InfoCard;
