import { useSelector } from "react-redux";

const EvaluationIndicator = () => {
  const riskLevel = useSelector((state) => state.evaluation.riskLevel);
  if (!riskLevel) return null;

  const riskLevels = {
    NONE: { label: "Aucun risque", background: "green", color: "white" },
    BORDERLINE: {
      label: "Risque borderline",
      background: "yellow",
      color: "black",
    },
    IN_DANGER: { label: "Risque danger", background: "orange", color: "white" },
    EARLY_ONSET: { label: "Risque précoce", background: "red", color: "white" },
  };

  const label = riskLevels[riskLevel].label;
  const background = riskLevels[riskLevel].background;
  const color = riskLevels[riskLevel].color;

  return (
    <div
      style={{
        display: "inline-block",
        padding: "6px 12px",
        borderRadius: "6px",
        backgroundColor: background,
        color: color,
        fontWeight: "bold",
      }}
    >
      {label}
    </div>
  );
};

export default EvaluationIndicator;
