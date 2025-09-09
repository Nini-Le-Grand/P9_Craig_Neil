export const loginUrl = `/login`
export const errorUrl = `/error`

export const profileUrl = `/profile`
export const profileUpdateUrl = `${profileUrl}/update`
export const passwordUpdateUrl = `${profileUrl}/password`

export const patientsUrl = `/patients`
export const patientUrl = (patientId) => `${patientsUrl}/${patientId}`
export const patientCreateUrl = `${patientsUrl}/create`
export const patientUpdateUrl = (patientId) => `${patientUrl(patientId)}/update`

export const notesUrl = (patientId) => `${patientUrl(patientId)}/notes`
export const noteUrl = (patientId, noteId) => `${notesUrl(patientId)}/${noteId}`
export const noteCreateUrl = (patientId) => `${notesUrl(patientId)}/create`
export const noteUpdateUrl = (patientId, noteId) => `${noteUrl(patientId, noteId)}/update`

export const usersUrl = `/users`
export const userUrl = (userId) => `${usersUrl}/${userId}`
export const userCreateUrl = `${usersUrl}/create`
export const userUpdateUrl = (userId) => `${userUrl(userId)}/update`