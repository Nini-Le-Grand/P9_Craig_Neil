  const formatDate = (dateTime) => {
    const dateObj = new Date(dateTime);
    const formattedDate = dateObj.toLocaleDateString("fr-FR");
    const formattedTime = dateObj.toLocaleTimeString("fr-FR", {
      hour: "2-digit",
      minute: "2-digit",
    });
    return `le ${formattedDate} à ${formattedTime}`;
  };

  export default formatDate;