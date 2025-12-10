import { useState, useEffect, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";
import axios from "axios";
import "./InterventionForm.css";

const AddIntervention = () => {
  const navigate = useNavigate();
  const { token, service } = useContext(AuthContext); // récupère token + service

  const [formData, setFormData] = useState({
    datePrevue: "",
    localisation: "",
    statut: "planifiée",
    type: "",
    efficacite: "Moyenne",
    techniciens: [], // IDs sélectionnés
    urgence: "Moyenne",
    priorite: "Moyenne",
    service: service || "", // sauvegarde service
  });

  const [techniciensList, setTechniciensList] = useState([]);
  const [selectedTechniciens, setSelectedTechniciens] = useState([]);

  // Charger tous les techniciens
  useEffect(() => {
    const fetchTechniciens = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/techniciens", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setTechniciensList(response.data);
      } catch (err) {
        console.error("Erreur fetch techniciens:", err);
      }
    };
    fetchTechniciens();
  }, [token]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleTechnicienSelect = (e, id) => {
    let newSelected;
    if (e.target.checked) {
      newSelected = [...selectedTechniciens, id];
    } else {
      newSelected = selectedTechniciens.filter((t) => t !== id);
    }
    setSelectedTechniciens(newSelected);
    setFormData({ ...formData, techniciens: newSelected });
  };

  const generateRandomLevel = () => {
    const options = ["Faible", "Moyenne", "Élevée"];
    return options[Math.floor(Math.random() * options.length)];
  };

  const handleMeasure = (field) => {
    const value = generateRandomLevel();
    setFormData({ ...formData, [field]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const techniciensObj = selectedTechniciens.map((id) => {
        const t = techniciensList.find((tech) => tech.id === id);
        return { id: t.id, nom: t.nom, prenom: t.prenom, specialite: t.specialite };
      });

      const interventionToAdd = {
        id: Date.now(),
        datePrevue: formData.datePrevue,
        localisation: formData.localisation,
        statut: formData.statut,
        type: formData.type,
        efficacite: formData.efficacite,
        techniciens: techniciensObj,
        urgence: formData.urgence,
        priorite: formData.priorite,
        service: formData.service, // sauvegarde le service
      };

      await axios.post("http://localhost:8080/api/interventions", interventionToAdd, {
        headers: { Authorization: `Bearer ${token}` },
      });

      // Redirection vers la liste du service
      navigate(`/${service}/interventions`);
    } catch (error) {
      console.error("Erreur lors de l'ajout :", error);
      alert("Erreur lors de l'ajout de l'intervention.");
    }
  };

  return (
    <div className="intervention-form-container">
      <h2>Ajouter une Intervention</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Date prévue :</label>
          <input
            type="date"
            name="datePrevue"
            value={formData.datePrevue}
            onChange={handleChange}
            required
          />
        </div>

        <div>
          <label>Localisation :</label>
          <input
            type="text"
            name="localisation"
            value={formData.localisation}
            placeholder="Localisation"
            onChange={handleChange}
            required
          />
        </div>

        <div>
          <label>Statut :</label>
          <select name="statut" value={formData.statut} onChange={handleChange}>
            <option value="planifiée">Planifiée</option>
            <option value="en_cours">En cours</option>
            <option value="terminée">Terminée</option>
          </select>
        </div>

        <div>
          <label>Type :</label>
          <input type="text" name="type" value={formData.type} onChange={handleChange} />
        </div>

        {/* Techniciens */}
        <div className="techniciens-container">
          <label>Techniciens :</label>
          <div className="techniciens-list">
            {techniciensList.map((tech) => (
              <label key={tech.id} className="technicien-card">
                <input
                  type="checkbox"
                  value={tech.id}
                  checked={selectedTechniciens.includes(tech.id)}
                  onChange={(e) => handleTechnicienSelect(e, tech.id)}
                />
                <div className="technicien-info">
                  <span>{tech.nom} {tech.prenom}</span>
                  <span className="specialite">{tech.specialite}</span>
                </div>
              </label>
            ))}
          </div>
        </div>

        {/* Mesure */}
        <div className="measure-section">
          <label>Efficacité : <strong>{formData.efficacite}</strong></label>
          <button type="button" onClick={() => handleMeasure("efficacite")}>Mesurer</button>
        </div>

        <div className="measure-section">
          <label>Urgence : <strong>{formData.urgence}</strong></label>
          <button type="button" onClick={() => handleMeasure("urgence")}>Mesurer</button>
        </div>

        <div className="measure-section">
          <label>Priorité : <strong>{formData.priorite}</strong></label>
          <button type="button" onClick={() => handleMeasure("priorite")}>Mesurer</button>
        </div>

        <div>
          <button type="submit">Ajouter</button>
        </div>
      </form>
    </div>
  );
};

export default AddIntervention;
