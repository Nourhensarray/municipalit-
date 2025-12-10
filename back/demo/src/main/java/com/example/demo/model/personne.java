package com.example.demo.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Technicien.class}) // JAXB doit connaître les sous-classes
public class personne {

    @XmlElement(namespace = "http://example.com/personne")
    private long id;

    @XmlElement(namespace = "http://example.com/personne")
    private String nom;

    @XmlElement(namespace = "http://example.com/personne")
    private String prenom;

    @XmlElement(namespace = "http://example.com/personne")
    private String email;

    @XmlElement(namespace = "http://example.com/personne")
    private String adresse;
    @XmlElement(namespace = "http://example.com/personne")
    private String password; 
    @XmlElement(namespace = "http://example.com/personne")
    private String telephone;

    public personne() {}

    public personne(long id, String nom, String prenom, String email, String adresse, String telephone, String password) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.telephone = telephone;
        this.password = password;  
    }

    // Getters et setters
    public long getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
