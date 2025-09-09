const Container = ({ children }) => {
  return <div style={styles}>{children}</div>;
};

const styles = {
  padding: "20px",
  borderRadius: "8px",
  boxShadow: "0 1px 4px rgba(0,0,0,0.1)",
  margin: "20px auto",
  maxWidth: "500px",
  backgroundColor: "#fff",
};

export default Container;
