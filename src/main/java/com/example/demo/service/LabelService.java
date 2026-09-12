package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Label;
import com.example.demo.exception.LabelNotFoundException;
import com.example.demo.repository.LabelRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Transactional(readOnly = true)
    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Label getById(long id) {
        return labelRepository.findById(id).orElseThrow(() -> new LabelNotFoundException(id));
    }

    @Transactional
    public Label create(Label label) {
        return labelRepository.save(label);
    }

    @Transactional
    public Label update(long id, Label updatedLabel) {
        Label existingLabel = labelRepository.findById(id).orElseThrow(() -> new LabelNotFoundException(id));
        existingLabel.setName(updatedLabel.getName());
        existingLabel.setColor(updatedLabel.getColor());

        return labelRepository.save(existingLabel);
    }

    @Transactional
    public void delete(long id) {
        if (!labelRepository.existsById(id)) {
            throw new LabelNotFoundException(id);
        }
        labelRepository.deleteLabelAssociations(id);
        labelRepository.deleteById(id);
    }
}
