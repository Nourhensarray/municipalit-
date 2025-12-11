package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Intervention;
import com.example.demo.service.XmlServiceJaxb;
import com.example.demo.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/interventions")
public class interventionController {

    @Autowired
    private XmlServiceJaxb xmlServiceJaxb;

    @Autowired
    private JwtService jwtService;

    /**
     * 🔒 GET — interventions réservées au ResponsableMunicipal connecté
     */
   @GetMapping("/my-service")
public List<Intervention> getByService(HttpServletRequest request) throws Exception {
    String token = jwtService.extractJwtFromRequest(request);
    String service = jwtService.extractService(token);
    return xmlServiceJaxb.getInterventionsByService(service);
}


    /**
     * GET — toutes les interventions (admin uniquement)
     */
    @GetMapping
    public List<Intervention> getAll() throws Exception {
        return xmlServiceJaxb.getAll();
    }

    @GetMapping("/new-id")
    public int getNewId() throws Exception {
        return xmlServiceJaxb.generateNewId();
    }

    @PostMapping
    public String add(@RequestBody Intervention i) throws Exception {
        xmlServiceJaxb.addIntervention(i);
        return "Intervention ajoutée";
    }

    @GetMapping("/sort/priorite")
    public List<Intervention> getSortedByPriorite() throws Exception {
        return xmlServiceJaxb.getInterventionsSortedByPriorite();
    }

    @GetMapping("/sort/urgence")
    public List<Intervention> getSortedByUrgence() throws Exception {
        return xmlServiceJaxb.getInterventionsSortedByUrgence();
    }

    @PutMapping("/{id}")
    public String update(@PathVariable int id, @RequestBody Intervention i) throws Exception {
        i.setIdInter(id);
        boolean ok = xmlServiceJaxb.updateIntervention(i);
        return ok ? "Intervention modifiée" : "ID non trouvé";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) throws Exception {
        boolean ok = xmlServiceJaxb.deleteIntervention(id);
        return ok ? "Intervention supprimée" : "ID non trouvé";
    }

    @GetMapping("/validate")
    public String validate() {
        return xmlServiceJaxb.validateXmlWithXsd() ? "XML valide" : "XML invalide";
    }
}
