import ListItem from "./ListItem";

const List = ({ items, label, actionLabel, actionOnClick }) => {
  return (
    <ul style={styles.list}>
      {items.map((item) => (
        <ListItem
          key={item.id}
          actionLabel={actionLabel(item)}
          onAction={() => actionOnClick(item)}
        >
          {label(item)}
        </ListItem>
      ))}
    </ul>
  );
};

const styles = {
  list: {
    listStyleType: "none",
    padding: 0,
    margin: 0,
    marginTop: "8px",
  },
};

export default List;
