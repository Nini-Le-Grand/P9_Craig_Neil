import { useState } from "react";

const Search = ({ placeholder, onSearch }) => {
  const [keyword, setKeyword] = useState("");

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      onSearch(keyword);
    }
  };

  const handleClick = () => {
    onSearch(keyword);
  };

  return (
    <div style={styles.container}>
      <input
        type="text"
        placeholder={placeholder}
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        onKeyDown={handleKeyDown}
        style={styles.input}
      />
      <button onClick={handleClick} style={styles.button}>
        Rechercher
      </button>
    </div>
  );
};

const styles = {
  container: {
    marginTop: "20px",
    marginBottom: "20px",
    display: "flex",
    gap: "10px",
  },
  input: {
    flex: 1,
    padding: "8px",
  },
  button: {
    padding: "8px 12px",
  },
};

export default Search;
