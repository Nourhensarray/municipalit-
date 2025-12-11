import { useState, useEffect, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";
import axios from "axios";
import "./InterventionForm.css";
import { getAllTechniciens } from "../services/technicienService";
import { getAllMateriels } from "../services/materielService";

const AddIntervention = () => {
  const navigate = useNavigate();
  const { token, service } = useContext(AuthContext);

  const [formData, setFormData] = useState({
    datePrevue: "",
    localisation: "",
    statut: "planifiée",
    type: "",
    efficacite: "Moyenne",
    techniciens: [],
    urgence: "Moyenne",
    priorite: "Moyenne",
    service: service || "",
    materiels: [],
  });

  const [techniciensList, setTechniciensList] = useState([]);
  const [materielsList, setMaterielsList] = useState([]);
  const [newMateriel, setNewMateriel] = useState({ categorie: "", quantite: 1 });

  // Charger techniciens et matériels
  useEffect(() => {
    const fetchData = async () => {
      try {
        const techs = await getAllTechniciens(token);
        setTechniciensList(Array.isArray(techs) ? techs : []);
        
        const mats = await getAllMateriels(token);
        setMaterielsList(Array.isArray(mats) ? mats : []);
      } catch (err) {
        console.error("Erreur fetch:", err);
        setTechniciensList([]);
        setMaterielsList([]);
      }
    };
    fetchData();
  }, [token]);

  // ------------------------
  // Handlers
  // ------------------------
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleTechnicienSelect = (e, idTech) => {
    const updated = e.target.checked
      ? [...formData.techniciens, idTech]
      : formData.techniciens.filter((t) => t !== idTech);
    setFormData({ ...formData, techniciens: updated });
  };

  const handleMaterielSelect = (e, mat) => {
  if (e.target.checked) {
    // Toujours définir une quantité valide
    const qty = mat.quantite && mat.quantite > 0 ? mat.quantite : 1;

    setFormData({
      ...formData,
      materiels: [...formData.materiels, { ...mat, quantite: qty }],
    });
  } else {
    setFormData({
      ...formData,
      materiels: formData.materiels.filter((m) => m.idM !== mat.idM),
    });
  }
};

  

  const generateRandomLevel = () => {
    const options = ["Faible", "Moyenne", "Élevée"];
    return options[Math.floor(Math.random() * options.length)];
  };

  const handleMeasure = (field) => {
    setFormData({ ...formData, [field]: generateRandomLevel() });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const techniciensObj = formData.techniciens
        .map((id) => {
          const t = techniciensList.find((tech) => tech.id === id);
          return t
            ? { id: t.id, nom: t.nom, prenom: t.prenom, specialite: t.specialite }
            : null;
        })
        .filter(Boolean);

      const interventionToAdd = {
        idInter: Date.now(),
        ...formData,
        techniciens: techniciensObj,
        materiels: formData.materiels.map((m) => ({
          idM: m.idM || 0,
          categorie: m.categorie || "",
          quantite: Number.isInteger(m.quantite) && m.quantite > 0 ? m.quantite : 1,
          dateAchat: m.dateAchat || null,
          etat: m.etat || "Neuf",
          valeur: m.valeur || "",
        })),
      };

      await axios.post("http://localhost:8080/api/interventions", interventionToAdd, {
        headers: { Authorization: `Bearer ${token}` },
      });

      navigate(`/${service}/interventions`);
    } catch (error) {
      console.error("Erreur lors de l'ajout :", error);
      alert("Erreur lors de l'ajout de l'intervention.");
    }
  };

  // ------------------------
  // Render
  // ------------------------
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
                  checked={formData.techniciens.includes(tech.id)}
                  onChange={(e) => handleTechnicienSelect(e, tech.id)}
                />
                <div className="technicien-info">
                  <span>
                    {tech.nom} {tech.prenom}
                  </span>
                  <span className="specialite">{tech.specialite}</span>
                </div>
              </label>
            ))}
          </div>
        </div>

 {/* Matériels */}
<div className="materiels-container">
  <label>Matériels :</label>
  <div className="materiels-list">
    {materielsList.map((m) => {
      const selectedMateriel = formData.materiels.find((sm) => sm.idM === m.idM);
      return (
        <label key={m.idM} className="materiel-card">
          <input
            type="checkbox"
            checked={!!selectedMateriel}
            onChange={(e) => handleMaterielSelect(e, m)}
          />
          <span>{m.categorie}</span>
          {selectedMateriel && (
  <input
    type="number"
    min="1"
    value={selectedMateriel.quantite}
    onChange={(e) => {
      const value = e.target.value;
      const qty = value === "" ? 1 : parseInt(value, 10);
      setFormData({
        ...formData,
        materiels: formData.materiels.map((sm) =>
          sm.idM === selectedMateriel.idM ? { ...sm, quantite: qty } : sm
        ),
      });
    }}
    style={{ width: "60px" }}
  />
)}

        </label>
      );
    })}
  </div>
</div>


        {/* Mesures */}
        {["efficacite", "urgence", "priorite"].map((field) => (
          <div className="measure-section" key={field}>
            <label>
              {field.charAt(0).toUpperCase() + field.slice(1)} :{" "}
              <strong>{formData[field]}</strong>
            </label>
            <button type="button" onClick={() => handleMeasure(field)}>
              Mesurer
            </button>
          </div>
        ))}

        <button type="submit">Ajouter</button>
      </form>
    </div>
  );
};

export default AddIntervention;
