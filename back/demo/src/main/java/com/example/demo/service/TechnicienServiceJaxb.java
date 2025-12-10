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

@Service
public class TechnicienServiceJaxb {

    private File getFile() throws Exception {
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
