
package com.example.demo.controller;

import com.example.demo.model.Materiel;
import com.example.demo.service.MaterielServiceJaxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/materiels")
public class MaterielController {

    @Autowired
    private MaterielServiceJaxb materielService;

    @GetMapping
    public List<Materiel> getAllMateriels() throws Exception {
        return materielService.lister();
    }

    @PostMapping
    public Materiel addMateriel(@RequestBody Materiel materiel) throws Exception {
        materielService.ajouter(materiel);
        return materiel;
    }

    @DeleteMapping("/{id}")
    public void deleteMateriel(@PathVariable int id) throws Exception {
        materielService.supprimer(id);
    }
}
