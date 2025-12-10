package com.example.demo.model;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;


import java.util.List;

@XmlRootElement(name = "users", namespace = "http://example.com/personne")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersWrapper {

    @XmlElement(name = "responsable", namespace = "http://example.com/personne")
    private List<ResponsableMunicipal> responsables;

    public List<ResponsableMunicipal> getResponsables() {
        return responsables;
    }
}
