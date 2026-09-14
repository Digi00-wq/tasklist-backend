package com.example.demo.controller;

import com.example.demo.dto.LabelDTO;
import com.example.demo.service.LabelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    public ResponseEntity<List<LabelDTO>> getAll(@RequestParam(required = false) Long projectId) {
        return ResponseEntity.ok(labelService.getAll(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(labelService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LabelDTO> create(@RequestBody LabelDTO dto) {
        LabelDTO created = labelService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelDTO> update(@PathVariable Long id, @RequestBody LabelDTO dto) {
        LabelDTO updated = labelService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        labelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
