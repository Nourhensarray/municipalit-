package com.example.demo.service;

import com.example.demo.model.ResponsableMunicipal;
import com.example.demo.model.UsersWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UsersWrapper users;

    public AuthService() {
        try {
            // Charger le fichier Users.xml depuis src/main/resources/data/
            ClassPathResource resource = new ClassPathResource("data/Users.xml");

            if (!resource.exists()) {
                System.err.println("❌ Users.xml introuvable dans src/main/resources/data !");
                return;
            }

            // Créer le contexte JAXB pour UsersWrapper
            JAXBContext context = JAXBContext.newInstance(UsersWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            this.users = (UsersWrapper) unmarshaller.unmarshal(resource.getInputStream());

            // Debug : vérifier que les utilisateurs sont bien chargés
            int count = users != null && users.getResponsables() != null ? users.getResponsables().size() : 0;
            System.out.println("✅ Users loaded: " + count);

            if (users != null && users.getResponsables() != null) {
                users.getResponsables().forEach(r -> {
                    String email = r.getEmail() != null ? r.getEmail() : "(null)";
                    String password = r.getPassword() != null ? r.getPassword() : "(null)";
                    System.out.println("Email: '" + email + "' | Password: '" + password + "'");
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Login d'un ResponsableMunicipal
     */
    public ResponsableMunicipal login(String email, String password) {
        if (users == null || users.getResponsables() == null) return null;

        return users.getResponsables()
                .stream()
                .filter(u -> u.getEmail() != null && u.getPassword() != null)
                .filter(u -> u.getEmail().trim().equalsIgnoreCase(email.trim()) &&
                             u.getPassword().trim().equals(password.trim()))
                .findFirst()
                .orElse(null);
    }
}
