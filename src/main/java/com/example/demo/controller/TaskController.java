package com.example.demo.controller;

import com.example.demo.dto.CreateTaskDTO;
import com.example.demo.dto.TaskDTO;
import com.example.demo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAll(@RequestParam(required = false) Long projectId,
                                 @RequestParam(required = false) String statusTab) {
        return taskService.getAll(projectId, statusTab);
    }

    @GetMapping("/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@RequestBody CreateTaskDTO dto) {
        return taskService.create(dto);
    }

    @PutMapping("/{id}")
    public TaskDTO update(@PathVariable Long id, @RequestBody CreateTaskDTO dto) {
        return taskService.update(id, dto);
    }

    @PatchMapping("/{id}/complete")
    public TaskDTO toggleComplete(@PathVariable Long id, @RequestParam(required = false) Boolean completed) {
        return taskService.toggleCompleted(id, completed);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
