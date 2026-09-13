package com.example.demo.service;

import com.example.demo.dto.LabelDTO;
import com.example.demo.entity.Label;
import com.example.demo.entity.Project;
import com.example.demo.exception.LabelNotFoundException;
import com.example.demo.exception.ProjectNotFoundException;
import com.example.demo.repository.LabelRepository;
import com.example.demo.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabelService {

    private final LabelRepository labelRepository;
    private final ProjectRepository projectRepository;

    public LabelService(LabelRepository labelRepository, ProjectRepository projectRepository) {
        this.labelRepository = labelRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<LabelDTO> getAll(Long projectId) {
        List<Label> labels;
        if (projectId != null) {
            labels = labelRepository.findByProjectId(projectId);
        } else {
            labels = labelRepository.findAll();
        }
        return labels.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LabelDTO getById(Long id) {
        Label label = labelRepository.findById(id).orElseThrow(() -> new LabelNotFoundException(id));
        return toDTO(label);
    }

    @Transactional
    public LabelDTO create(LabelDTO dto) {
        Label label = new Label();
        label.setName(dto.getName());
        label.setColor(dto.getColor());
        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));
            label.setProject(project);
        }
        Label saved = labelRepository.save(label);
        return toDTO(saved);
    }

    @Transactional
    public LabelDTO update(Long id, LabelDTO dto) {
        Label label = labelRepository.findById(id).orElseThrow(() -> new LabelNotFoundException(id));
        label.setName(dto.getName());
        label.setColor(dto.getColor());
        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));
            label.setProject(project);
        } else {
            label.setProject(null);
        }
        Label saved = labelRepository.save(label);
        return toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!labelRepository.existsById(id)) {
            throw new LabelNotFoundException(id);
        }
        labelRepository.deleteLabelAssociations(id);
        labelRepository.deleteById(id);
    }

    public LabelDTO toDTO(Label label) {
        if (label == null) return null;
        Long projectId = label.getProject() != null ? label.getProject().getId() : null;
        return new LabelDTO(label.getId(), label.getName(), label.getColor(), projectId);
    }
}
