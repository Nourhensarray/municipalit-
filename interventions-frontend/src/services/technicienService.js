import axios from "axios";

const API_URL = "http://localhost:8080/api/techniciens"; // endpoint backend

// Récupérer tous les techniciens
export const getAllTechniciens = async () => {
  try {
    const response = await axios.get(API_URL);
    return response.data; // devrait être un tableau [{id, nom, prenom, specialite}, ...]
  } catch (error) {
    console.error("Erreur lors de la récupération des techniciens :", error);
    return [];
  }
};

// Récupérer un technicien par ID (optionnel)
export const getTechnicienById = async (id) => {
  try {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du technicien ${id} :`, error);
    return null;
  }
};

// Ajouter un technicien (optionnel)
export const addTechnicien = async (technicien) => {
  try {
    const response = await axios.post(API_URL, technicien);
    return response.data;
  } catch (error) {
    console.error("Erreur lors de l'ajout du technicien :", error);
    return null;
  }
};

// Mettre à jour un technicien (optionnel)
export const updateTechnicien = async (technicien) => {
  try {
    const response = await axios.put(`${API_URL}/${technicien.id}`, technicien);
    return response.data;
  } catch (error) {
    console.error("Erreur lors de la mise à jour du technicien :", error);
    return null;
  }
};

// Supprimer un technicien (optionnel)
export const deleteTechnicien = async (id) => {
  try {
    const response = await axios.delete(`${API_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error("Erreur lors de la suppression du technicien :", error);
    return null;
  }
};
