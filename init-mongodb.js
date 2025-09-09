db = db.getSiblingDB("medilabo");

db.notes.insertMany([
  {
    patientId: "d20f4060-1462-4c41-ab90-416649a3002f",
    dateTime: new Date("2025-01-01T09:00:00"),
    note: "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé"
  },
  {
    patientId: "5f6df173-4293-4dd6-ba69-8828bfadb27f",
    dateTime: new Date("2025-02-15T14:30:00"),
    note: "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement"
  },
  {
    patientId: "5f6df173-4293-4dd6-ba69-8828bfadb27f",
    dateTime: new Date("2025-03-10T11:45:00"),
    note: "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale"
  },
  {
    patientId: "07602259-8b21-4d7c-9b83-c391582176e5",
    dateTime: new Date("2025-04-20T10:15:00"),
    note: "Le patient déclare qu'il fume depuis peu"
  },
  {
    patientId: "07602259-8b21-4d7c-9b83-c391582176e5",
    dateTime: new Date("2025-05-05T16:00:00"),
    note: "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé"
  },
  {
    patientId: "5230f742-542e-4906-b257-70c03e13d9b1",
    dateTime: new Date("2025-06-12T09:30:00"),
    note: "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments"
  },
  {
    patientId: "5230f742-542e-4906-b257-70c03e13d9b1",
    dateTime: new Date("2025-07-01T14:00:00"),
    note: "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps"
  },
  {
    patientId: "5230f742-542e-4906-b257-70c03e13d9b1",
    dateTime: new Date("2025-08-18T11:00:00"),
    note: "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé"
  },
  {
    patientId: "5230f742-542e-4906-b257-70c03e13d9b1",
    dateTime: new Date("2025-09-05T15:30:00"),
    note: "Taille, Poids, Cholestérol, Vertige et Réaction"
  }
]);
