package com.example.demo.service;

import com.example.demo.model.Intervention;
import com.example.demo.model.Interventions;
import com.example.demo.model.Materiel;
import com.example.demo.model.Technicien;
import com.example.demo.service.MaterielServiceJaxb;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.transform.stream.StreamSource;

@Service
public class XmlServiceJaxb {
    @Autowired
    private TechnicienServiceJaxb technicienService;
    @Autowired
    private MaterielServiceJaxb materielServiceJaxb; // injection correcte

   @Value("${interventions.file:}")
private String interventionsFilePath;

private File getFile() throws Exception {
    if (interventionsFilePath != null && !interventionsFilePath.isBlank()) {
        File file = new File(interventionsFilePath);
        
        // Si c'est un chemin relatif (commençant par src/), le convertir en absolu
        if (!file.isAbsolute()) {
            file = file.getAbsoluteFile();
        }
        
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        if (!file.exists()) {
            ClassPathResource resource = new ClassPathResource("data/interventions.xml");
            if (resource.exists()) {
                try (java.io.InputStream in = resource.getInputStream()) {
                    java.nio.file.Files.copy(in, file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("✅ interventions.xml copié vers: " + file.getAbsolutePath());
                }
            }
        }
        
        System.out.println("📁 Fichier interventions: " + file.getAbsolutePath());
        return file;
    }

    // Fallback
    ClassPathResource resource = new ClassPathResource("data/interventions.xml");
    return resource.getFile();
}
    

    // ============================
    // LIRE TOUTES LES INTERVENTIONS
    // ============================
    public List<Intervention> getAll() throws Exception {
        JAXBContext context = JAXBContext.newInstance(Interventions.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Interventions list = (Interventions) unmarshaller.unmarshal(getFile());

        if (list.getInterventions() == null) {
            list.setInterventions(new ArrayList<>());
        }

        return list.getInterventions();
    }

    // ============================
    // AJOUTER UNE INTERVENTION
    // ============================
    public void addIntervention(Intervention intervention) {
    try {
        JAXBContext context = JAXBContext.newInstance(Interventions.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        Interventions list = (Interventions) unmarshaller.unmarshal(getFile());

        if (list.getInterventions() == null) {
            list.setInterventions(new ArrayList<>());
        }

        // ⭐ GÉNÉRER NOUVEL ID ⭐
        int newId = (int) (list.getInterventions()
                        .stream()
                        .mapToLong(Intervention::getIdInter)
                        .max()
                        .orElse(0) + 1);

        intervention.setIdInter(newId);
        List<Materiel> materiels = materielServiceJaxb.lister(); // ou filtrer ceux que tu veux
intervention.setMateriels(materiels);
        // Ajouter intervention dans la liste générale
        list.getInterventions().add(intervention);

        // Ajouter cette intervention à chaque technicien concerné
        for (Technicien t : intervention.getTechniciens()) {
            technicienService.addInterventionToTechnicien((int) t.getId(), intervention);
        }
        

        // Sauvegarder
        marshaller.marshal(list, getFile());

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // ============================
    // SUPPRIMER UNE INTERVENTION
    // ============================
    public boolean deleteIntervention(int id) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Interventions.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Interventions list = (Interventions) unmarshaller.unmarshal(getFile());

        if (list.getInterventions() == null) {
            return false;
        }

        boolean removed = list.getInterventions().removeIf(i -> i.getIdInter() == id);

        if (removed) {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(list, getFile());
        }

        return removed;
    }

    // ============================
    // METTRE À JOUR UNE INTERVENTION
    // ============================
    public boolean updateIntervention(Intervention newData) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Interventions.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Interventions list = (Interventions) unmarshaller.unmarshal(getFile());

        if (list.getInterventions() == null) {
            return false;
        }

        boolean updated = false;

        for (Intervention i : list.getInterventions()) {
            if (i.getIdInter() == newData.getIdInter()) {
                i.setDatePrevue(newData.getDatePrevue());
                i.setLocalisation(newData.getLocalisation());
                i.setStatut(newData.getStatut());
                i.setType(newData.getType());
                i.setEfficacite(newData.getEfficacite());
                i.setTechniciens(newData.getTechniciens());
                i.setPriorite(newData.getPriorite());
                i.setMateriels(newData.getMateriels());
                i.setUrgence(newData.getUrgence());
                updated = true;
                break;
            }
        }

        if (updated) {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(list, getFile());
        }

        return updated;
    }

    // ============================
    // VALIDATION XSD
    // ============================
    public boolean validateXmlWithXsd() {
        try {
            File xml = getFile();
            File xsd = ResourceUtils.getFile("classpath:data/interventions.xsd");

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsd);
            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(xml));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============================
    // TRI PAR PRIORITÉ
    // ============================
    public List<Intervention> getInterventionsSortedByPriorite() throws Exception {
        List<Intervention> interventions = getAll();
        List<String> ordrePriorite = List.of("élevée", "moyenne", "faible");

        interventions.sort((a, b) ->
            Integer.compare(
                ordrePriorite.indexOf(a.getPriorite().toLowerCase()),
                ordrePriorite.indexOf(b.getPriorite().toLowerCase())
            )
        );

        return interventions;
    }

    // ============================
    // TRI PAR URGENCE
    // ============================
    public List<Intervention> getInterventionsSortedByUrgence() throws Exception {
        List<Intervention> interventions = getAll();
        List<String> ordreUrgence = List.of("élevée", "moyenne", "faible");

        interventions.sort((a, b) ->
            Integer.compare(
                ordreUrgence.indexOf(a.getUrgence().toLowerCase()),
                ordreUrgence.indexOf(b.getUrgence().toLowerCase())
            )
        );

        return interventions;
    }

    // ============================
    // GENERER NOUVEL ID
    // ============================
    public int generateNewId() throws Exception {
        List<Intervention> list = getAll();
        return list.stream().mapToInt(Intervention::getIdInter).max().orElse(0) + 1;
    }
public List<Intervention> getInterventionsByService(String service) throws Exception {
    return getAll().stream()
            .filter(i -> i.getService().equalsIgnoreCase(service))
            .toList();
}


}