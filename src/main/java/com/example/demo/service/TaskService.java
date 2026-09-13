package com.example.demo.service;

import com.example.demo.dto.CreateTaskDTO;
import com.example.demo.dto.LabelDTO;
import com.example.demo.dto.TaskDTO;
import com.example.demo.entity.Label;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.exception.ProjectNotFoundException;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.repository.LabelRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final LabelRepository labelRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.labelRepository = labelRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getAll(Long projectId, String statusTab) {
        List<Task> tasks;
        if (projectId != null && statusTab != null) {
            tasks = taskRepository.findByProjectIdAndStatusTab(projectId, statusTab);
        } else if (projectId != null) {
            tasks = taskRepository.findByProjectId(projectId);
        } else if (statusTab != null) {
            tasks = taskRepository.findByStatusTab(statusTab);
        } else {
            tasks = taskRepository.findAll();
        }
        return tasks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskDTO getById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return toDTO(task);
    }

    @Transactional
    public TaskDTO create(CreateTaskDTO dto) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.getCompleted() != null ? dto.getCompleted() : false);
        task.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : Instant.now());
        task.setStatusTab(dto.getStatusTab());

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));
            task.setProject(project);
        }

        if (dto.getLabels() != null) {
            task.setLabels(resolveLabels(dto.getLabels(), task.getProject()));
        }

        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    @Transactional
    public TaskDTO update(Long id, CreateTaskDTO dto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        if (dto.getCompleted() != null) {
            task.setCompleted(dto.getCompleted());
        }
        if (dto.getTimestamp() != null) {
            task.setTimestamp(dto.getTimestamp());
        }
        if (dto.getStatusTab() != null) {
            task.setStatusTab(dto.getStatusTab());
        }

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));
            task.setProject(project);
        }

        task.getLabels().clear();
        if (dto.getLabels() != null) {
            task.getLabels().addAll(resolveLabels(dto.getLabels(), task.getProject()));
        }

        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    @Transactional
    public TaskDTO update(Long id, TaskDTO dto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        if (dto.getName() != null) {
            task.setName(dto.getName());
        }
        task.setDescription(dto.getDescription());
        if (dto.getCompleted() != null) {
            task.setCompleted(dto.getCompleted());
        }
        if (dto.getTimestamp() != null) {
            task.setTimestamp(dto.getTimestamp());
        }
        if (dto.getStatusTab() != null) {
            task.setStatusTab(dto.getStatusTab());
        }

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));
            task.setProject(project);
        }

        if (dto.getLabels() != null) {
            task.getLabels().clear();
            task.getLabels().addAll(resolveLabels(dto.getLabels(), task.getProject()));
        }

        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    @Transactional
    public TaskDTO patch(Long id, TaskDTO dto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        if (dto.getName() != null) {
            task.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }
        if (dto.getCompleted() != null) {
            task.setCompleted(dto.getCompleted());
        }
        if (dto.getTimestamp() != null) {
            task.setTimestamp(dto.getTimestamp());
        }
        if (dto.getStatusTab() != null) {
            task.setStatusTab(dto.getStatusTab());
        }
        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));
            task.setProject(project);
        }
        if (dto.getLabels() != null) {
            task.getLabels().clear();
            task.getLabels().addAll(resolveLabels(dto.getLabels(), task.getProject()));
        }

        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    @Transactional
    public TaskDTO toggleCompleted(Long id, Boolean completed) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setCompleted(completed != null ? completed : !task.getCompleted());
        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    private List<Label> resolveLabels(List<LabelDTO> labelDTOs, Project project) {
        List<Label> resolved = new ArrayList<>();
        for (LabelDTO dto : labelDTOs) {
            if (dto.getId() != null) {
                Label existing = labelRepository.findById(dto.getId()).orElse(null);
                if (existing != null) {
                    resolved.add(existing);
                } else {
                    Label newLabel = new Label();
                    newLabel.setName(dto.getName());
                    newLabel.setColor(dto.getColor());
                    newLabel.setProject(project);
                    resolved.add(labelRepository.save(newLabel));
                }
            } else {
                Label newLabel = new Label();
                newLabel.setName(dto.getName());
                newLabel.setColor(dto.getColor());
                newLabel.setProject(project);
                resolved.add(labelRepository.save(newLabel));
            }
        }
        return resolved;
    }

    public TaskDTO toDTO(Task task) {
        if (task == null) return null;
        Long projectId = task.getProject() != null ? task.getProject().getId() : null;

        List<LabelDTO> labelDTOs = task.getLabels() != null ?
                task.getLabels().stream()
                        .map(lbl -> new LabelDTO(lbl.getId(), lbl.getName(), lbl.getColor(),
                                lbl.getProject() != null ? lbl.getProject().getId() : null))
                        .collect(Collectors.toList()) : List.of();

        return new TaskDTO(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getCompleted(),
                task.getTimestamp(),
                task.getStatusTab(),
                projectId,
                labelDTOs
        );
    }
}
