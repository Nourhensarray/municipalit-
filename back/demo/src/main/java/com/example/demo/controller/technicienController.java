package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.TechnicienServiceJaxb;
import com.example.demo.model.Technicien;

@RestController
@RequestMapping("/api/techniciens")
@CrossOrigin(origins = "http://localhost:5173")
public class technicienController {

    @Autowired
    private TechnicienServiceJaxb technicienService;

    @GetMapping
    public List<Technicien> getAllTechniciens() throws Exception  {
        return technicienService.getAll();   
    }
}
