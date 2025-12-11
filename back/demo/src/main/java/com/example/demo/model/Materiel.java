package com.example.demo.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;
import com.example.demo.adapter.LocalDateAdapter;

@XmlRootElement(name = "materiel")
@XmlAccessorType(XmlAccessType.FIELD)
public class Materiel {

    private long idM;

    private String categorie;
     @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateAchat;

    private String etat;
    private int quantite = 1;  // Valeur par défaut à 1

    private String valeur;

    // === Constructors ===
    public Materiel() {}

    public Materiel(long idM, String categorie, int quantite) {
        this.idM = idM;
        this.categorie = categorie;
        this.quantite = quantite > 0 ? quantite : 1;
    }

    // === Getters et Setters ===

    public long getIdM() {
        return idM;
    }

    public void setIdM(long idM) {
        this.idM = idM;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    public Integer getQuantite() {
    return quantite;
}

public void setQuantite(Integer quantite) {
    this.quantite = quantite != null && quantite > 0 ? quantite : 1;
}

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat = dateAchat;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}
