package com.example.demo.service;



import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.demo.model.Materiel;
import com.example.demo.model.Materiels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterielServiceJaxb {

    @Value("${materiels.file:}")
    private String materielsPath;

    // ---------------------
    //  FICHIER XML
    // ---------------------
    private File getFile() throws Exception {

        if (materielsPath != null && !materielsPath.isBlank()) {
            File file = new File(materielsPath);

            if (!file.exists()) {
                ClassPathResource resource = new ClassPathResource("data/materiels.xml");
                java.nio.file.Files.copy(resource.getInputStream(), file.toPath());
            }

            return file;
        }

        return new ClassPathResource("data/materiels.xml").getFile();
    }

    // ---------------------
    //  Lister
    // ---------------------
    public List<Materiel> lister() throws Exception {
        JAXBContext context = JAXBContext.newInstance(Materiels.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Materiels list = (Materiels) unmarshaller.unmarshal(getFile());

        if (list.getMateriels() == null)
            list.setMateriels(new ArrayList<>());

        return list.getMateriels();
    }

    // ---------------------
    //  Ajouter
    // ---------------------
    public void ajouter(Materiel materiel) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Materiels.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        Materiels list = (Materiels) unmarshaller.unmarshal(getFile());

        if (list.getMateriels() == null)
            list.setMateriels(new ArrayList<>());

        int newId = (int) (list.getMateriels().stream().mapToLong(Materiel::getIdM).max().orElse(0) + 1);
        materiel.setIdM(newId);

        list.getMateriels().add(materiel);

        marshaller.marshal(list, getFile());
    }

    // ---------------------
    //  Modifier
    // ---------------------
    public boolean modifier(Materiel newData) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Materiels.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Marshaller marshaller = context.createMarshaller();

        Materiels list = (Materiels) unmarshaller.unmarshal(getFile());

        boolean updated = false;

        for (Materiel m : list.getMateriels()) {
            if (m.getIdM() == newData.getIdM()) {
                m.setCategorie(newData.getCategorie());
                m.setDateAchat(newData.getDateAchat());
                m.setEtat(newData.getEtat());
                m.setValeur(newData.getValeur());
                m.setQuantite(newData.getQuantite());

                updated = true;
                break;
            }
        }

        if (updated) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(list, getFile());
        }

        return updated;
    }

    // ---------------------
    //  Supprimer
    // ---------------------
    public boolean supprimer(int id) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Materiels.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Marshaller marshaller = context.createMarshaller();

        Materiels list = (Materiels) unmarshaller.unmarshal(getFile());

        boolean deleted = list.getMateriels().removeIf(m -> m.getIdM() == id);

        if (deleted) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(list, getFile());
        }

        return deleted;
    }
}
