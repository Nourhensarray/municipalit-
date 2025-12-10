import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getAllTechniciens } from "../services/technicienService";
import { getAllInterventions, updateIntervention } from "../services/interventionService";
import "./InterventionForm.css";


const EditIntervention = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState(null);
  const [techniciensList, setTechniciensList] = useState([]);

  useEffect(() => {
    const fetchTechniciens = async () => {
      const data = await getAllTechniciens();
      setTechniciensList(data);
    };
    fetchTechniciens();
  }, []);

  useEffect(() => {
    const fetchIntervention = async () => {
      const all = await getAllInterventions();
      const intervention = all.find((i) => i.idInter === parseInt(id));
      setFormData(intervention);
    };
    fetchIntervention();
  }, [id]);

  if (!formData) return <div>Chargement...</div>;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleTechniciensChange = (e) => {
    const options = Array.from(e.target.selectedOptions);
    const ids = options.map((o) => parseInt(o.value));
    setFormData({ ...formData, techniciens: ids });
  };

  const handleSubmit = async (e) => {
  e.preventDefault();

  try {
    // Créer la liste complète des techniciens valides
    const techniciensObj = (formData.techniciens || [])
      .map((id) => {
        const t = techniciensList.find((tech) => tech.id === id);
        if (!t) return null; // ignore si introuvable
        return {
          id: t.id,
          nom: t.nom || "",
          prenom: t.prenom || "",
        };
      })
      .filter(Boolean); // supprime les null

    // Construire l'objet intervention à envoyer au backend
    const interventionToUpdate = {
      idInter: formData.idInter,                   // ID existant
      datePrevue: formData.datePrevue || "",      
      localisation: formData.localisation || "",  
      statut: formData.statut || "planifiée",
      type: formData.type || "",
      efficacite: formData.efficacite || "Moyenne",
      techniciens: techniciensObj,
      urgence: formData.urgence
        ? formData.urgence.charAt(0).toUpperCase() + formData.urgence.slice(1)
        : "Moyenne",
      priorite: formData.priorite
        ? formData.priorite.charAt(0).toUpperCase() + formData.priorite.slice(1)
        : "Moyenne",
    };

    await axios.put(
      `http://localhost:8080/api/interventions/${interventionToUpdate.idInter}`,
      interventionToUpdate,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    // Redirection vers la liste du service
    navigate(`/${formData.service}/interventions`);

  } catch (error) {
    console.error("Erreur lors de la modification de l'intervention :", error);
    alert("Erreur lors de la modification. Vérifie la console.");
  }
};


  return (<div className="intervention-form-container">
  <h2>Modifier une Intervention</h2>
    <form onSubmit={handleSubmit}>
      <div>
        <label>Date prévue :</label>
        <input type="date" name="datePrevue" value={formData.datePrevue} onChange={handleChange} required />
      </div>

      <div>
        <label>Localisation :</label>
        <input type="text" name="localisation" value={formData.localisation} onChange={handleChange} required />
      </div>

      <div>
        <label>Type :</label>
        <input type="text" name="type" value={formData.type} onChange={handleChange} required />
      </div>

      
  <div className="techniciens-container">
  <label>Techniciens :</label>
  <div className="techniciens-list">
    {techniciensList.map((tech) => (
      <label key={tech.id} className="technicien-card">
        <input
          type="checkbox"
          value={tech.id}
          checked={formData.techniciens.includes(tech.id)}
          onChange={(e) => {
            const id = tech.id;
            let newSelected = [...formData.techniciens];
            if (e.target.checked) {
              newSelected.push(id);
            } else {
              newSelected = newSelected.filter((tid) => tid !== id);
            }
            setFormData({ ...formData, techniciens: newSelected });
          }}
        />
        <div className="technicien-info">
          <strong>{tech.nom} {tech.prenom}</strong>
          <span className="specialite">{tech.specialite}</span>
        </div>
      </label>
    ))}
  </div>
</div>




      <button type="submit">Modifier</button>
    </form></div>
  );
};

export default EditIntervention;
