import Fields from "./Fields";
import SubmitButton from "./SubmitButton";
import CancelButton from "./buttons/CancelButton";

const Form = ({
  fields,
  values,
  fieldErrors,
  loading,
  onChange,
  onSubmit,
  onCancel,
  submitLabel = "Enregistrer",
  cancelLabel = "Annuler",
}) => {
  return (
    <form onSubmit={onSubmit} style={styles.form}>
      <Fields
        fields={fields}
        values={values}
        onChange={onChange}
        fieldErrors={fieldErrors}
      />

      <SubmitButton disabled={loading} label={loading ? "Envoi…" : submitLabel} />
      {onCancel && <CancelButton onClick={onCancel} label={cancelLabel} />}
    </form>
  );
};

const styles = {
  form: { display: "flex", flexDirection: "column", gap: "12px" },
};

export default Form;
