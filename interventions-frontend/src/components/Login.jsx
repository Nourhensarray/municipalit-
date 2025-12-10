import React, { useState, useContext } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";

const Login = () => {
  const { login } = useContext(AuthContext);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const findToken = (data) =>
    data?.token || data?.jwtToken || data?.accessToken || data?.authToken || null;

  const findService = (data) =>
    data?.service || data?.role || data?.type || data?.department || null;

  const routeForService = (svc) => {
    if (!svc) return "/"; // fallback
    switch (svc.toLowerCase()) {
      case "transport":
        return "/transport/interventions";
      case "securite":
      case "sécurité":
        return "/securite/interventions";
      case "voirie":
        return "/voirie/interventions";
      case "assainissement":
        return "/assainissement/interventions";
      default:
        return "/";
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await axios.post(
        "http://localhost:8080/auth/login",
        null,
        { params: { email, password } } // si ton backend attend @RequestParam
      );

      // DEBUG : montre la réponse (supprime en production)
      console.log("login response.data =", response.data);

      const data = response.data || {};
      const token = findToken(data);
      const service = findService(data);
      const errMsg = data?.error || data?.message || null;

      if (errMsg) {
        setError(errMsg);
        return;
      }

      if (!token) {
        setError("Aucun token reçu du serveur — vérifie la réponse (console).");
        return;
      }

      // Enregistre dans le contexte (AuthContext gère localStorage)
      login(token, service);

      // Redirection selon service (fallback = "/")
      const target = routeForService(service);
      navigate(target, { replace: true });
    } catch (err) {
      console.error("Login error:", err);
      // Si le backend renvoie une réponse structurée d'erreur, essaie de l'afficher
      const serverMsg = err?.response?.data?.error || err?.response?.data?.message;
      setError(serverMsg || "Email ou mot de passe invalide");
    }
  };

  return (
    <div style={{ maxWidth: "400px", margin: "auto", padding: "2rem" }}>
      <h2>Connexion</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Mot de passe"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        {error && <p style={{ color: "red" }}>{error}</p>}
        <button type="submit">Se connecter</button>
      </form>
    </div>
  );
};

export default Login;
