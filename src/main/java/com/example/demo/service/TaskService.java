package com.example.demo.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Label;
import com.example.demo.entity.Task;
import com.example.demo.exception.LabelNotFoundException;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.repository.LabelRepository;
import com.example.demo.repository.TaskRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final LabelRepository labelRepository;

    public TaskService(TaskRepository taskRepository, LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.labelRepository = labelRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task getById(long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task create(Task task) {
        if (task.getCompleted() == null) {
            task.setCompleted(false);
        }
        if (task.getTimestamp() == null) {
            task.setTimestamp(Instant.now());
        }
        if (task.getLabels() != null) {
            task.setLabels(resolveLabels(task.getLabels()));
        }
        return taskRepository.save(task);
    }

    @Transactional
    public Task update(long id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        existingTask.setName(updatedTask.getName());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.getCompleted() != null ? updatedTask.getCompleted() : false);
        if (updatedTask.getTimestamp() != null) {
            existingTask.setTimestamp(updatedTask.getTimestamp());
        } else if (existingTask.getTimestamp() == null) {
            existingTask.setTimestamp(Instant.now());
        }

        existingTask.getLabels().clear();
        if (updatedTask.getLabels() != null) {
            existingTask.getLabels().addAll(resolveLabels(updatedTask.getLabels()));
        }

        return taskRepository.save(existingTask);
    }

    @Transactional
    public void delteTask(long id) {
        taskRepository.deleteById(id);
    }

    private List<Label> resolveLabels(List<Label> labels) {
        List<Label> resolved = new ArrayList<>();
        for (Label label : labels) {
            if (label.getId() != null) {
                Label existing = labelRepository.findById(label.getId())
                        .orElseThrow(() -> new LabelNotFoundException(label.getId()));
                resolved.add(existing);
            } else {
                resolved.add(labelRepository.save(label));
            }
        }
        return resolved;
    }

}
