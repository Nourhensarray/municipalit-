package com.example.demo.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "responsable",namespace = "http://example.com/personne")
public class ResponsableMunicipal extends personne {
    @XmlElement(namespace = "http://example.com/personne")
    private String service;

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
}
