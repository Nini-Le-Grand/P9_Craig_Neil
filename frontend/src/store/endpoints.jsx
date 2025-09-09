export const mainEndpoint = process.env.REACT_APP_API_URL;

export const userMsEndpoint = `${mainEndpoint}${process.env.REACT_APP_USER_MS}`;
export const noteMsEndpoint = `${mainEndpoint}${process.env.REACT_APP_NOTE_MS}`;
export const evaluationMsEndpoint = `${mainEndpoint}${process.env.REACT_APP_EVALUATION_MS}`;

export const loginEndpoint = `${userMsEndpoint}/auth/login`

export const getProfileEndpoint = `${userMsEndpoint}/user/profile`
export const updateProfileEndpoint = `${userMsEndpoint}/user/profile`
export const updatePasswordEndpoint = `${userMsEndpoint}/password/update`

export const getPatientsEndpoint = `${userMsEndpoint}/patients`
export const createPatientEndpoint = `${userMsEndpoint}/patients`
export const getPatientEndpoint = (patientId) => `${userMsEndpoint}/patients/${patientId}`
export const updatePatientEndpoint = (patientId) => `${userMsEndpoint}/patients/${patientId}`
export const deletePatientEndpoint = (patientId) => `${userMsEndpoint}/patients/${patientId}`

export const createUserEndpoint = `${userMsEndpoint}/admin/users`
export const getUserEndpoint = (userId) => `${userMsEndpoint}/admin/users/${userId}`
export const updateUserEndpoint = (userId) => `${userMsEndpoint}/admin/users/${userId}`
export const deleteUserEndpoint = (userId) => `${userMsEndpoint}/admin/users/${userId}`
export const resetUserPasswordEndpoint = (userId) => `${userMsEndpoint}/admin/users/${userId}`
export const searchUsersEndpoint = (keyword) => `${userMsEndpoint}/admin/users/search?keyword=${keyword}`

export const createNoteEndpoint = `${noteMsEndpoint}`
export const getNotesEndpoint = (patientId) => `${noteMsEndpoint}/${patientId}`
export const updateNoteEndpoint = (noteId) => `${noteMsEndpoint}/${noteId}`
export const deleteNoteEndpoint = (noteId) => `${noteMsEndpoint}/${noteId}`

export const getEvaluationEndpoint = (patientId) => `${evaluationMsEndpoint}/${patientId}`