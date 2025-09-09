const patientFields = [
  {
    label: "Prénom",
    name: "firstName",
    type: "text",
    placeholder: "Entrez le prénom",
  },
  {
    label: "Nom",
    name: "lastName",
    type: "text",
    placeholder: "Entrez le nom",
  },
  {
    label: "Email",
    name: "email",
    type: "email",
    placeholder: "exemple@mail.com",
  },
  {
    label: "Téléphone",
    name: "phone",
    type: "text",
    placeholder: "06XXXXXXXX",
  },
  { label: "Date de naissance", name: "dateOfBirth", type: "date" },
  {
    label: "Adresse",
    name: "address",
    type: "text",
    placeholder: "Entrez l'adresse",
  },
  {
    label: "Sexe",
    name: "gender",
    type: "radio",
    options: [
      { value: "F", label: "Femme" },
      { value: "M", label: "Homme" },
    ],
  },
];

export default patientFields;
