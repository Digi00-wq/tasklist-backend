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

import java.time.Instant;
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
    void testCreateProjectWithTabsAndTasks() {
        CreateProjectDTO projectDTO = new CreateProjectDTO();
        projectDTO.setName("Ferien Projekt");
        projectDTO.setColor("#e44232");
        projectDTO.setTabs(List.of(new ProjectTabDTO("not_started", "Offen", "#e44232", "Noch nicht begonnen")));

        ProjectDTO createdProject = projectController.create(projectDTO);
        assertNotNull(createdProject.getId());
        assertEquals("Ferien Projekt", createdProject.getName());
        assertEquals(1, createdProject.getTabs().size());

        CreateTaskDTO taskDTO = new CreateTaskDTO();
        taskDTO.setName("Ferien eingeben");
        taskDTO.setDescription("Optional notes");
        taskDTO.setCompleted(false);
        taskDTO.setProjectId(createdProject.getId());
        taskDTO.setStatusTab("not_started");
        taskDTO.setLabels(List.of(new LabelDTO(null, "Urgent", "#e5484d", createdProject.getId())));

        TaskDTO createdTask = taskController.create(taskDTO);
        assertNotNull(createdTask.getId());
        assertEquals("Ferien eingeben", createdTask.getName());
        assertEquals(createdProject.getId(), createdTask.getProjectId());
        assertEquals(1, createdTask.getLabels().size());
        assertEquals("Urgent", createdTask.getLabels().get(0).getName());
    }

    @Test
    void testDeleteLabelWithTaskAssociation() {
        CreateProjectDTO projectDTO = new CreateProjectDTO("Work", "#333333", List.of());
        ProjectDTO project = projectController.create(projectDTO);

        LabelDTO labelDTO = labelController.create(new LabelDTO(null, "WorkLabel", "#94a3b8", project.getId()));

        CreateTaskDTO taskDTO = new CreateTaskDTO();
        taskDTO.setName("Work Task");
        taskDTO.setProjectId(project.getId());
        taskDTO.setLabels(List.of(labelDTO));
        TaskDTO task = taskController.create(taskDTO);

        assertDoesNotThrow(() -> labelController.delete(labelDTO.getId()));

        TaskDTO refreshedTask = taskController.getById(task.getId());
        assertTrue(refreshedTask.getLabels().isEmpty());
    }
}
