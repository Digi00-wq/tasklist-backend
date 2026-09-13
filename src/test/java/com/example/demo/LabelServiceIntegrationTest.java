package com.example.demo;

import com.example.demo.controller.LabelController;
import com.example.demo.controller.ProjectController;
import com.example.demo.controller.TaskController;
import com.example.demo.dto.*;
import com.example.demo.repository.LabelRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LabelServiceIntegrationTest {

    @Autowired
    private ProjectController projectController;

    @Autowired
    private TaskController taskController;

    @Autowired
    private LabelController labelController;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void testCreateAndUpdateProjectTabsWithPosition() {
        CreateProjectDTO projectDTO = new CreateProjectDTO();
        projectDTO.setName("Kanban Project");
        projectDTO.setColor("#e44232");
        projectDTO.setTabs(List.of(
                new ProjectTabDTO("todo", "To Do", "Offene Aufgaben", "#e44232", 0),
                new ProjectTabDTO("in_progress", "In Progress", "In Arbeit", "#3b82f6", 1),
                new ProjectTabDTO("review_123", "Code Review", "Review Phase", "#8b5cf6", 2)
        ));

        ResponseEntity<ProjectDTO> response = projectController.create(projectDTO);
        ProjectDTO createdProject = response.getBody();
        assertNotNull(createdProject);
        assertEquals(3, createdProject.getTabs().size());
        assertEquals("todo", createdProject.getTabs().get(0).getId());
        assertEquals(0, createdProject.getTabs().get(0).getPosition());

        // Update tabs order and fields
        projectDTO.setTabs(List.of(
                new ProjectTabDTO("in_progress", "In Progress", "Aktive Arbeit", "#3b82f6", 0),
                new ProjectTabDTO("todo", "To Do", "Warteschlange", "#e44232", 1)
        ));

        ResponseEntity<ProjectDTO> updateResponse = projectController.updateProject(createdProject.getId(), projectDTO);
        ProjectDTO updatedProject = updateResponse.getBody();
        assertNotNull(updatedProject);
        assertEquals(2, updatedProject.getTabs().size());
        assertEquals("in_progress", updatedProject.getTabs().get(0).getId());
        assertEquals(0, updatedProject.getTabs().get(0).getPosition());
    }

    @Test
    void testUpdateAndPatchTaskStatusTab() {
        CreateProjectDTO projectDTO = new CreateProjectDTO("Project A", "#10b981", List.of(
                new ProjectTabDTO("not_started", "Not Started", "Subtitle", "#10b981", 0),
                new ProjectTabDTO("started", "Started", "Subtitle", "#f59e0b", 1)
        ));
        ProjectDTO project = projectController.create(projectDTO).getBody();

        CreateTaskDTO taskDTO = new CreateTaskDTO();
        taskDTO.setName("Task 1");
        taskDTO.setProjectId(project.getId());
        taskDTO.setStatusTab("not_started");
        TaskDTO createdTask = taskController.create(taskDTO).getBody();
        assertNotNull(createdTask);
        assertEquals("not_started", createdTask.getStatusTab());
        assertFalse(createdTask.getCompleted());

        // Test PUT /api/tasks/{id}
        TaskDTO putUpdate = new TaskDTO();
        putUpdate.setName("Task 1 Updated");
        putUpdate.setStatusTab("started");
        putUpdate.setCompleted(true);
        putUpdate.setProjectId(project.getId());

        ResponseEntity<TaskDTO> putResponse = taskController.updateTask(createdTask.getId(), putUpdate);
        TaskDTO updatedTask = putResponse.getBody();
        assertNotNull(updatedTask);
        assertEquals("started", updatedTask.getStatusTab());
        assertTrue(updatedTask.getCompleted());

        // Test PATCH /api/tasks/{id}
        TaskDTO patchUpdate = new TaskDTO();
        patchUpdate.setStatusTab("completed");

        ResponseEntity<TaskDTO> patchResponse = taskController.patchTask(createdTask.getId(), patchUpdate);
        TaskDTO patchedTask = patchResponse.getBody();
        assertNotNull(patchedTask);
        assertEquals("completed", patchedTask.getStatusTab());
        assertEquals("Task 1 Updated", patchedTask.getName()); // name preserved
    }
}
