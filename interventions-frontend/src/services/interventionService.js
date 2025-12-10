import axios from "axios";

const API_URL = "http://localhost:8080/api/interventions";

export const getAllInterventions = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

export const getInterventionsByPriorite = async () => {
  const response = await axios.get(`${API_URL}/sort/priorite`);
  return response.data;
};

export const getInterventionsByUrgence = async () => {
  const response = await axios.get(`${API_URL}/sort/urgence`);
  return response.data;
};
export const getNewInterventionId = async () => {
  const response = await axios.get(`${API_URL}/new-id`);
  return response.data;
};

export const getInterventionsByPrioriteUrgence = async () => {
  const response = await axios.get(`${API_URL}/sort/priorite-urgence`);
  return response.data;
};

export const addIntervention = async (intervention) => {
  const response = await axios.post(API_URL, intervention);
  return response.data;
};

export const updateIntervention = async (intervention) => {
  const response = await axios.put(`${API_URL}/${intervention.idInter}`, intervention);
  return response.data;
};
