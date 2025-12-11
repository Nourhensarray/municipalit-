package com.example.demo.model;



import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@XmlRootElement(name = "materiels")
@XmlAccessorType(XmlAccessType.FIELD)
public class Materiels {
    
    @JsonProperty("materiels")
    @XmlElement(name = "materiel")
    private List<Materiel> materiels;

    public List<Materiel> getMateriels() {
        return materiels;
    }

    public void setMateriels(List<Materiel> materiels) {
        this.materiels = materiels;
    }
}
