import { useState, useEffect, useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "./AuthContext";
import axios from "axios";

const ListInterventions = () => {
  const [interventions, setInterventions] = useState([]);
  const { token } = useContext(AuthContext);

  useEffect(() => {
    fetchInterventions();
  }, []);

  const fetchInterventions = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/interventions/my-service", {
        headers: { Authorization: `Bearer ${token}` },
      });

      const data = Array.isArray(response.data) ? response.data : response.data.interventions || [];
      setInterventions(data);
    } catch (error) {
      console.error("Erreur lors du fetch des interventions :", error);
      setInterventions([]);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Voulez-vous vraiment supprimer cette intervention ?")) {
      await axios.delete(`http://localhost:8080/api/interventions/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      fetchInterventions();
    }
  };

  return (
    <div>
      <h2>Liste des interventions</h2>

      <div style={{ marginBottom: "15px" }}>
        <Link to="/add" style={{ marginLeft: "15px" }}>Ajouter une intervention</Link>
      </div>

      <table border="1" cellPadding="8">
        <thead>
          <tr>
            <th>ID</th>
            <th>Date prévue</th>
            <th>Localisation</th>
            <th>Statut</th>
            <th>Type</th>
            <th>Efficacité</th>
            <th>Urgence</th>
            <th>Priorité</th>
            <th>Techniciens</th>
            <th>Matériels</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {(interventions || []).map((inter) => (
            <tr key={inter.idInter}>
              <td>{inter.idInter}</td>
              <td>{inter.datePrevue}</td>
              <td>{inter.localisation}</td>
              <td>{inter.statut}</td>
              <td>{inter.type}</td>
              <td>{inter.efficacite}</td>
              <td>{inter.urgence}</td>
              <td>{inter.priorite}</td>
              <td>{(inter.techniciens || []).map(t => `${t.nom} ${t.prenom}`).join(", ")}</td>
              <td>
                {(inter.materiels && inter.materiels.length > 0)
                  ? inter.materiels.map(m => `${m.categorie} (x${m.quantite})`).join(", ")
                  : "—"}
              </td>
              <td>
                <div className="action-buttons">
                  <Link to={`/edit/${inter.idInter}`}>
                    <button>Modifier</button>
                  </Link>
                  <button onClick={() => handleDelete(inter.idInter)}>Supprimer</button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ListInterventions;