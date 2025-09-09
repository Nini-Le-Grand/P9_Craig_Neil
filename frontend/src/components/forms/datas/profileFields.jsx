const profileFields = [
  {
    name: "firstName",
    label: "Prénom",
    type: "text",
    placeholder: "Entrez votre prénom",
  },
  {
    name: "lastName",
    label: "Nom",
    type: "text",
    placeholder: "Entrez votre nom",
  },
  {
    name: "email",
    label: "Email",
    type: "email",
    placeholder: "exemple@mail.com",
  },
  {
    name: "gender",
    label: "Sexe",
    type: "radio",
    options: [
      { value: "M", label: "Homme" },
      { value: "F", label: "Femme" },
    ],
  },
  {
    name: "dateOfBirth",
    label: "Date de naissance",
    type: "date",
  },
  {
    name: "address",
    label: "Adresse",
    type: "text",
    placeholder: "Entrez votre adresse",
  },
  {
    name: "phone",
    label: "Téléphone",
    type: "text",
    placeholder: "06XXXXXXXX",
  },
];

export default profileFields;
