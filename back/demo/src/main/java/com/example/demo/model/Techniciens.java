package com.example.demo.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "techniciens",namespace = "http://example.com/personne")
public class Techniciens {

    private List<Technicien> techniciens;

    @XmlElement(name = "technicien", namespace = "http://example.com/personne")
    public List<Technicien> getTechniciens() {
        return techniciens;
    }

    public void setTechniciens(List<Technicien> techniciens) {
        this.techniciens = techniciens;
    }
}
