package com.example.demo.service;

import com.example.demo.model.Intervention;
import com.example.demo.model.Technicien;
import com.example.demo.model.Techniciens;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;


@Service
public class TechnicienServiceJaxb {
@Value("${techniciens.file:}")
private String techniciensFilePath;

private File getFile() throws Exception {
    if (techniciensFilePath != null && !techniciensFilePath.isBlank()) {
        File file = new File(techniciensFilePath);
        
        if (!file.isAbsolute()) {
            file = file.getAbsoluteFile();
        }
        
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        if (!file.exists()) {
            ClassPathResource resource = new ClassPathResource("data/technicien.xml");
            if (resource.exists()) {
                try (java.io.InputStream in = resource.getInputStream()) {
                    java.nio.file.Files.copy(in, file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("✅ technicien.xml copié vers: " + file.getAbsolutePath());
                }
            }
        }
        
        System.out.println("📁 Fichier techniciens: " + file.getAbsolutePath());
        return file;
    }

    return ResourceUtils.getFile("data/technicien.xml");
}

    // ======================
    // AJOUT INTERVENTION
    // ======================
    public void addInterventionToTechnicien(int idTech, Intervention inter) throws Exception {

        JAXBContext context = JAXBContext.newInstance(Techniciens.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Techniciens wrapper = (Techniciens) unmarshaller.unmarshal(getFile());

        for (Technicien t : wrapper.getTechniciens()) {

            if (t.getId() == idTech) {

                if (t.getInterventions() == null)
                    t.setInterventions(new ArrayList<>());

                t.getInterventions().add(inter);
            }
        }

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, getFile());
    }

    // ======================
    // GET ALL
    // ======================
    public List<Technicien> getAll() throws Exception {

        JAXBContext context = JAXBContext.newInstance(Techniciens.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Techniciens wrapper = (Techniciens) unmarshaller.unmarshal(getFile());
        return wrapper.getTechniciens();
    }

    // ======================
    // ADD TECHNICIEN
    // ======================
    public void add(Technicien technicien) throws Exception {

        JAXBContext context = JAXBContext.newInstance(Techniciens.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Techniciens wrapper = (Techniciens) unmarshaller.unmarshal(getFile());
        wrapper.getTechniciens().add(technicien);

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, getFile());
    }

    // ======================
    // DELETE TECHNICIEN
    // ======================
    public boolean delete(int id) throws Exception {

        JAXBContext context = JAXBContext.newInstance(Techniciens.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Techniciens wrapper = (Techniciens) unmarshaller.unmarshal(getFile());

        boolean removed = wrapper.getTechniciens().removeIf(t -> t.getId() == id);

        if (removed) {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrapper, getFile());
        }

        return removed;
    }

    // ======================
    // UPDATE TECHNICIEN
    // ======================
    public boolean update(Technicien newData) throws Exception {

        JAXBContext context = JAXBContext.newInstance(Techniciens.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Techniciens wrapper = (Techniciens) unmarshaller.unmarshal(getFile());

        boolean updated = false;

        for (Technicien t : wrapper.getTechniciens()) {

            if (t.getId() == newData.getId()) {
                t.setNom(newData.getNom());
                t.setPrenom(newData.getPrenom());
                t.setAdresse(newData.getAdresse());
                t.setTelephone(newData.getTelephone());
                t.setEmail(newData.getEmail());
                t.setSpecialite(newData.getSpecialite());
                updated = true;
                break;
            }
        }

        if (updated) {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrapper, getFile());
        }

        return updated;
    }

    // ======================
    // GENERATE ID
    // ======================
    public long generateNewId() throws Exception {

        return getAll()
                .stream()
                .mapToLong(Technicien::getId)
                .max()
                .orElse(0) + 1;
    }
}
