package com.example.demo.model;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement(name = "interventions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Interventions {

    @XmlElement(name = "intervention")
    private List<Intervention> interventions = new ArrayList<>(); // initialisation

    public List<Intervention> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<Intervention> interventions) {
        this.interventions = interventions;
    }
}
