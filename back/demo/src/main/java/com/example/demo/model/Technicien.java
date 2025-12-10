package com.example.demo.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "technicien", namespace = "http://example.com/personne")
@XmlAccessorType(XmlAccessType.FIELD)
public class Technicien extends personne {

    @XmlElement(name = "specialite", namespace = "http://example.com/personne")
    private String specialite;

    // ⭐ AJOUT LISTE DES INTERVENTIONS ⭐
    @XmlElementWrapper(name = "interventions")
    @XmlElement(name = "intervention")
    private List<Intervention> interventions = new ArrayList<>();

    public Technicien() {}

    public Technicien(long id, String nom, String prenom, String specialite,
                      String adresse, String email, String telephone,String password) {
        super(id, nom, prenom, email, adresse, telephone,password); // ordre correct
        this.specialite = specialite;
    }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    // ⭐ GETTER/SETTER INTERVENTIONS ⭐
    public List<Intervention> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<Intervention> interventions) {
        this.interventions = interventions;
    }
}
