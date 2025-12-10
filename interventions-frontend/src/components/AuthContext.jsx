import React, { createContext, useState, useEffect } from "react";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem("jwtToken") || null);
  const [service, setService] = useState(localStorage.getItem("service") || null);

  useEffect(() => {
    if (token) localStorage.setItem("jwtToken", token);
    else localStorage.removeItem("jwtToken");

    if (service) localStorage.setItem("service", service);
    else localStorage.removeItem("service");
  }, [token, service]);

  const login = (tokenValue, serviceValue) => {
    setToken(tokenValue);
    setService(serviceValue);

    // Optionnel mais sécurisé
    localStorage.setItem("jwtToken", tokenValue);
    localStorage.setItem("service", serviceValue);
  };

  const logout = () => {
    setToken(null);
    setService(null);

    localStorage.removeItem("jwtToken");
    localStorage.removeItem("service");
  };

  return (
    <AuthContext.Provider value={{ token, service, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
