package com.example.demo.controller;

import com.example.demo.dto.LabelDTO;
import com.example.demo.service.LabelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    public List<LabelDTO> getAll(@RequestParam(required = false) Long projectId) {
        return labelService.getAll(projectId);
    }

    @GetMapping("/{id}")
    public LabelDTO getById(@PathVariable Long id) {
        return labelService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@RequestBody LabelDTO dto) {
        return labelService.create(dto);
    }

    @PutMapping("/{id}")
    public LabelDTO update(@PathVariable Long id, @RequestBody LabelDTO dto) {
        return labelService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        labelService.delete(id);
    }
}
