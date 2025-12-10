import React, { useContext } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import AddIntervention from "./components/AddIntervention";
import EditIntervention from "./components/EditIntervention";
import ListInterventions from "./components/InterventionsList";
import Login from "./components/Login";
import { AuthProvider, AuthContext } from "./components/AuthContext";

const PrivateRoute = ({ children }) => {
  const { token } = useContext(AuthContext);
  if (!token) return <Navigate to="/login" replace />;
  return children;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>

          {/* Page de login */}
          <Route path="/login" element={<Login />} />

          {/* Routes par service */}
          <Route
            path="/transport/interventions"
            element={
              <PrivateRoute>
                <ListInterventions service="transport" />
              </PrivateRoute>
            }
          />

          <Route
            path="/securite/interventions"
            element={
              <PrivateRoute>
                <ListInterventions service="securite" />
              </PrivateRoute>
            }
          />

          <Route
            path="/voirie/interventions"
            element={
              <PrivateRoute>
                <ListInterventions service="voirie" />
              </PrivateRoute>
            }
          />

          <Route
            path="/assainissement/interventions"
            element={
              <PrivateRoute>
                <ListInterventions service="assainissement" />
              </PrivateRoute>
            }
          />

          {/* Routes générales */}
          <Route
            path="/add"
            element={
              <PrivateRoute>
                <AddIntervention />
              </PrivateRoute>
            }
          />

          <Route
            path="/edit/:id"
            element={
              <PrivateRoute>
                <EditIntervention />
              </PrivateRoute>
            }
          />

          {/* 🚀 IMPORTANT : Redirection par défaut */}
          <Route path="/" element={<Navigate to="/login" replace />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
