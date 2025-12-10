package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.adapter.LocalDateAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "intervention")
@XmlAccessorType(XmlAccessType.FIELD)
public class Intervention {

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "datePrevue")
    private LocalDate datePrevue;

    @XmlElement(name = "idInter")
    private int idInter;
    @XmlElement(name = "service")
    private String service;
    @XmlElement(name = "localisation")
    private String localisation;

    @XmlElement(name = "statut")
    private Statut statut;

    @XmlElement(name = "type")
    private String type;

    @XmlElement(name = "efficacite")
    private String efficacite;

    @XmlElementWrapper(name = "techniciens")
    @XmlElement(name = "technicien")
    private List<Technicien> techniciens;

    @XmlAttribute(name = "urgence")
    private String urgence;

    @XmlAttribute(name = "priorite")
    private String priorite;

    public enum Statut { planifiée, en_cours, terminée }

    // Constructeurs
    public Intervention() {}

    public Intervention(LocalDate datePrevue, int idInter, String localisation, Statut statut,
                        String type, String efficacite, List<Technicien> techniciens,
                        String urgence, String priorite) {
        this.datePrevue = datePrevue;
        this.idInter = idInter;
        this.localisation = localisation;
        this.statut = statut;
        this.type = type;
        this.efficacite = efficacite;
        this.techniciens = techniciens;
        this.urgence = urgence;
        this.priorite = priorite;
    }

    // Getters & Setters
    public LocalDate getDatePrevue() { return datePrevue; }
    public void setDatePrevue(LocalDate datePrevue) { this.datePrevue = datePrevue; }

    public int getIdInter() { return idInter; }
    public void setIdInter(int idInter) { this.idInter = idInter; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getEfficacite() { return efficacite; }
    public void setEfficacite(String efficacite) { this.efficacite = efficacite; }

    public List<Technicien> getTechniciens() { return techniciens; }
    public void setTechniciens(List<Technicien> techniciens) { this.techniciens = techniciens; }

    public String getUrgence() { return urgence; }
    public void setUrgence(String urgence) { this.urgence = urgence; }

    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    public String getService() { return service; }
    public void setService(String priorite) { this.service = priorite; }
}
