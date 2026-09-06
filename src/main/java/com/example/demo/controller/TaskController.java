package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/tasks") @CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAll() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable long id) {
        return taskService.getById(id);
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task) {
        return taskService.create(task);
    }

    @PutMapping("/{id}")
    public Task editTask(@PathVariable long id, @RequestBody Task task) {
        return taskService.update(id, task);
    }

    @DeleteMapping("/{id}")
    public void delteTask(@PathVariable long id) {
        taskService.delteTask(id);
    }

}
