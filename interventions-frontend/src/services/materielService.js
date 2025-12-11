import axios from "axios";

// URL de base pour l'API backend
const BASE_URL = "http://localhost:8080/api/materiels";

/**
 * Récupère tous les matériels depuis le backend.
 * @param {string} token - le token d'authentification
 * @returns {Promise<Array>} - liste des matériels
 */
export const getAllMateriels = async (token) => {
  try {
    const headers = token ? { Authorization: `Bearer ${token}` } : {};
    const response = await axios.get(BASE_URL, { headers });
    return response.data; // devrait renvoyer un tableau de matériels
  } catch (error) {
    console.error("Erreur fetch matériels:", error);
    return [];
  }
};

/**
 * Optionnel : ajouter un nouveau matériel
 */
export const addMateriel = async (materiel, token) => {
  try {
    const headers = token ? { Authorization: `Bearer ${token}` } : {};
    const response = await axios.post(BASE_URL, materiel, { headers });
    return response.data;
  } catch (error) {
    console.error("Erreur ajout matériel:", error);
    throw error;
  }
};

/**
 * Optionnel : supprimer un matériel
 */
export const deleteMateriel = async (idM, token) => {
  try {
    const headers = token ? { Authorization: `Bearer ${token}` } : {};
    const response = await axios.delete(`${BASE_URL}/${idM}`, { headers });
    return response.data;
  } catch (error) {
    console.error("Erreur suppression matériel:", error);
    throw error;
  }
};
