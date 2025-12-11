import { useState, useEffect, useContext } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { AuthContext } from "./AuthContext";
import { getAllTechniciens } from "../services/technicienService";
import { getAllMateriels } from "../services/materielService";
import axios from "axios";
import "./InterventionForm.css";

const EditIntervention = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { token, service } = useContext(AuthContext);

  const [formData, setFormData] = useState(null);
  const [techniciensList, setTechniciensList] = useState([]);
  const [materielsList, setMaterielsList] = useState([]);
  const [newMateriel, setNewMateriel] = useState({ categorie: "", quantite: 1 });

  // Charger techniciens & matériels
  useEffect(() => {
    const fetchLists = async () => {
      try {
        const techs = await getAllTechniciens(token);
        setTechniciensList(Array.isArray(techs) ? techs : []);

        const mats = await getAllMateriels(token);
        setMaterielsList(Array.isArray(mats) ? mats : []);
      } catch (err) {
        console.error("Erreur fetch techniciens/materiels:", err);
      }
    };

    fetchLists();
  }, [token]);

  // Charger intervention
  useEffect(() => {
    const fetchIntervention = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/interventions/my-service",
          { headers: { Authorization: `Bearer ${token}` } }
        );

        const all = Array.isArray(response.data)
          ? response.data
          : response.data.interventions || [];

        const intervention = all.find((i) => i.idInter === parseInt(id));

        if (intervention) {
          const techIds = intervention.techniciens?.map((t) => t.id) || [];

          setFormData({
            ...intervention,
            techniciens: techIds,
            materiels: intervention.materiels || []
          });
        }
      } catch (error) {
        console.error("Erreur fetch intervention:", error);
      }
    };

    fetchIntervention();
  }, [id, token]);

  if (!formData) return <div>Chargement...</div>;

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
      : formData.techniciens.filter((tid) => tid !== idTech);

    setFormData({ ...formData, techniciens: updated });
  };

  const handleMaterielSelect = (e, mat) => {
    if (e.target.checked) {
      setFormData({
        ...formData,
        materiels: [...formData.materiels, { ...mat, quantite: mat.quantite || 1 }]
      });
    } else {
      setFormData({
        ...formData,
        materiels: formData.materiels.filter((m) => m.idM !== mat.idM)
      });
    }
  };

  const addMateriel = () => {
    if (!newMateriel.categorie.trim() || newMateriel.quantite < 1) return;

    const generatedId = Date.now();
    const mat = { ...newMateriel, idM: generatedId };

    setMaterielsList([...materielsList, mat]);

    setFormData({
      ...formData,
      materiels: [...formData.materiels, mat]
    });

    setNewMateriel({ categorie: "", quantite: 1 });
  };

  const removeMateriel = (idM) => {
    setFormData({
      ...formData,
      materiels: formData.materiels.filter((m) => m.idM !== idM),
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const techniciensObj = formData.techniciens
        .map((idTech) => {
          const t = techniciensList.find((tech) => tech.id === idTech);
          return t
            ? {
                id: t.id,
                nom: t.nom,
                prenom: t.prenom,
                specialite: t.specialite,
              }
            : null;
        })
        .filter(Boolean);

      const payload = {
        ...formData,
        techniciens: techniciensObj,
        materiels: formData.materiels.map((m) => ({
          idM: m.idM,
          categorie: m.categorie,
          quantite: m.quantite,
          dateAchat: m.dateAchat || null,
          etat: m.etat || "Neuf",
          valeur: m.valeur || "",
        })),
      };

      await axios.put(
        `http://localhost:8080/api/interventions/${formData.idInter}`,
        payload,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      navigate(`/${service}/interventions`);
    } catch (error) {
      console.error("Erreur modification:", error);
      alert("Erreur lors de la modification.");
    }
  };

  // ------------------------
  // Render
  // ------------------------
  return (
    <div className="intervention-form-container">
      <h2>Modifier une Intervention</h2>

      <form onSubmit={handleSubmit}>
        {/* Champs standard */}
        <div>
          <label>Date prévue :</label>
          <input
            type="date"
            name="datePrevue"
            value={formData.datePrevue || ""}
            onChange={handleChange}
          />
        </div>

        <div>
          <label>Localisation :</label>
          <input
            type="text"
            name="localisation"
            value={formData.localisation || ""}
            onChange={handleChange}
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
                <strong>{tech.nom} {tech.prenom}</strong>
                <span>{tech.specialite}</span>
              </label>
            ))}
          </div>
        </div>

        {/* Matériels */}
        <div className="materiels-container">
          <label>Matériels :</label>

          <div className="materiels-list">
            {materielsList.map((m) => {
              const selected = formData.materiels.find((sm) => sm.idM === m.idM);

              return (
                <label key={m.idM} className="materiel-card">
                  <input
                    type="checkbox"
                    checked={!!selected}
                    onChange={(e) => handleMaterielSelect(e, m)}
                  />

                  <span>{m.categorie}</span>

                  {selected && (
                    <input
                      type="number"
                      min="1"
                      value={selected.quantite}
                      onChange={(e) => {
                        const qty = parseInt(e.target.value) || 1;
                        setFormData({
                          ...formData,
                          materiels: formData.materiels.map((sm) =>
                            sm.idM === m.idM ? { ...sm, quantite: qty } : sm
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

        <button type="submit">Modifier</button>
      </form>
    </div>
  );
};

export default EditIntervention;
