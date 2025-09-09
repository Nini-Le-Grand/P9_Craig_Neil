import TextInput from "./inputs/TextInput";
import TextAreaInput from "./inputs/TextAreaInput";
import RadioGroup from "./inputs/RadioGroup";

const Fields = ({ fields, values, onChange, fieldErrors = {} }) => {
  return (
    <>
      {fields.map((field) => {
        const error = fieldErrors?.[field.name];

        switch (field.type) {
          case "text":
          case "email":
          case "password":
          case "date":
          case "number":
            return (
              <TextInput
                key={field.name}
                {...field}
                value={values[field.name] || ""}
                onChange={onChange}
                error={error}
              />
            );

          case "textarea":
            return (
              <TextAreaInput
                key={field.name}
                {...field}
                value={values[field.name] || ""}
                onChange={onChange}
                error={error}
              />
            );

          case "radio":
            return (
              <RadioGroup
                key={field.name}
                {...field}
                value={values[field.name]}
                onChange={onChange}
                error={error}
              />
            );

          default:
            return null;
        }
      })}
    </>
  );
};

export default Fields;
