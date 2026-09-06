package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.service.LabelService;
import com.example.demo.entity.Label;

@RestController @RequestMapping("/api/labels") @CrossOrigin(origins = "*", allowedHeaders = "*")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    public List<Label> getAll() {
        return labelService.getAll();
    }

    @GetMapping("/{id}")
    public Label getById(@PathVariable long id) {
        return labelService.getById(id);
    }

    @PostMapping
    public Label create(@RequestBody Label label) {
        return labelService.create(label);
    }

    @PutMapping("/{id}")
    public Label update(@PathVariable long id, @RequestBody Label label) {
        return labelService.update(id, label);
    }

    @DeleteMapping("/{id}")
    public void delte(@PathVariable long id) {
        labelService.delete(id);
    }

}
