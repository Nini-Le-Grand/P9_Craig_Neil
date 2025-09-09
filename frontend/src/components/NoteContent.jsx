const NoteContent = ({ text }) => {
  if (!text) return null;

  return (
    <div style={styles.wrapper}>
      <p style={styles.note}>{text}</p>
    </div>
  );
};

const styles = {
  wrapper: {
    backgroundColor: "#f9f9f9",
    border: "1px solid #ddd",
    borderRadius: "8px",
    padding: "12px 16px",
    margin: "10px 0",
  },
  note: {
    whiteSpace: "pre-wrap",
    wordWrap: "break-word",
    fontSize: "15px",
    lineHeight: "1.6",
    color: "#333",
    margin: 0,
  },
};

export default NoteContent;
