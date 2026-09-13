package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Project;
import com.example.demo.entity.ProjectTab;
import com.example.demo.exception.ProjectNotFoundException;
import com.example.demo.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getAll() {
        return projectRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDTO getById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return toDTO(project);
    }

    @Transactional
    public ProjectDTO create(CreateProjectDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setColor(dto.getColor());
        if (dto.getTabs() != null) {
            project.setTabs(dto.getTabs().stream()
                    .map(t -> new ProjectTab(t.getId(), t.getName(), t.getColor(), t.getSubtitle()))
                    .collect(Collectors.toList()));
        }
        Project saved = projectRepository.save(project);
        return toDTO(saved);
    }

    @Transactional
    public ProjectDTO update(Long id, CreateProjectDTO dto) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        project.setName(dto.getName());
        project.setColor(dto.getColor());
        project.getTabs().clear();
        if (dto.getTabs() != null) {
            project.getTabs().addAll(dto.getTabs().stream()
                    .map(t -> new ProjectTab(t.getId(), t.getName(), t.getColor(), t.getSubtitle()))
                    .collect(Collectors.toList()));
        }
        Project saved = projectRepository.save(project);
        return toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectRepository.deleteById(id);
    }

    public ProjectDTO toDTO(Project project) {
        if (project == null) return null;

        List<ProjectTabDTO> tabDTOs = project.getTabs() != null ?
                project.getTabs().stream()
                        .map(t -> new ProjectTabDTO(t.getId(), t.getName(), t.getColor(), t.getSubtitle()))
                        .collect(Collectors.toList()) : List.of();

        List<LabelDTO> labelDTOs = project.getLabels() != null ?
                project.getLabels().stream()
                        .map(l -> new LabelDTO(l.getId(), l.getName(), l.getColor(), project.getId()))
                        .collect(Collectors.toList()) : List.of();

        List<TaskDTO> taskDTOs = project.getTasks() != null ?
                project.getTasks().stream()
                        .map(t -> new TaskDTO(
                                t.getId(),
                                t.getName(),
                                t.getDescription(),
                                t.getCompleted(),
                                t.getTimestamp(),
                                t.getStatusTab(),
                                project.getId(),
                                t.getLabels() != null ? t.getLabels().stream()
                                        .map(lbl -> new LabelDTO(lbl.getId(), lbl.getName(), lbl.getColor(),
                                                lbl.getProject() != null ? lbl.getProject().getId() : null))
                                        .collect(Collectors.toList()) : List.of()
                        ))
                        .collect(Collectors.toList()) : List.of();

        return new ProjectDTO(project.getId(), project.getName(), project.getColor(), tabDTOs, labelDTOs, taskDTOs);
    }
}
