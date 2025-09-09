import ListButton from "./ListButton";

const ListItem = ({ children, actionLabel, onAction }) => {
  return (
    <li style={styles.item}>
      <span>{children}</span>
      {actionLabel && onAction && (
        <ListButton label={actionLabel} onClick={onAction} />
      )}
    </li>
  );
};

const styles = {
  item: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "10px 15px",
    marginBottom: "10px",
    backgroundColor: "#fff",
    borderRadius: "6px",
    boxShadow: "0 1px 3px rgba(0,0,0,0.1)",
  },
};

export default ListItem;
