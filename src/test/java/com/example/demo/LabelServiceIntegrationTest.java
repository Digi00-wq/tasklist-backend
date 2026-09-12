package com.example.demo;

import com.example.demo.controller.LabelController;
import com.example.demo.controller.TaskController;
import com.example.demo.entity.Label;
import com.example.demo.entity.Task;
import com.example.demo.exception.LabelNotFoundException;
import com.example.demo.repository.LabelRepository;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LabelServiceIntegrationTest {

    @Autowired
    private TaskController taskController;

    @Autowired
    private LabelController labelController;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
    }

    @Test
    void testTaskDefaults() {
        // Create task with ONLY name
        Task task = new Task();
        task.setName("Minimal Task");

        Task createdTask = taskController.createTask(task);
        assertNotNull(createdTask.getId());
        assertEquals("Minimal Task", createdTask.getName());
        assertNull(createdTask.getDescription());
        assertFalse(createdTask.getCompleted()); // Defaults to false
        assertNotNull(createdTask.getTimestamp()); // Defaults to Instant.now()
        assertTrue(createdTask.getLabels().isEmpty()); // Labels can be empty
    }

    @Test
    void testTaskWithCustomTimestampAndDescription() {
        Instant customTimestamp = Instant.parse("2026-01-01T10:00:00Z");
        Task task = new Task();
        task.setName("Custom Task");
        task.setDescription("My description");
        task.setCompleted(true);
        task.setTimestamp(customTimestamp);

        Task createdTask = taskController.createTask(task);
        assertEquals("Custom Task", createdTask.getName());
        assertEquals("My description", createdTask.getDescription());
        assertTrue(createdTask.getCompleted());
        assertEquals(customTimestamp, createdTask.getTimestamp());
    }

    @Test
    void testLabelDefaults() {
        Label label = new Label();
        label.setName("Simple Label");

        Label createdLabel = labelController.create(label);
        assertEquals("Simple Label", createdLabel.getName());
        assertEquals("#000000FF", createdLabel.getColor()); // Defaults to #000000FF
    }

    @Test
    void testCreateTaskWithNewLabelAndColor() {
        Task task = new Task();
        task.setName("Test Task");
        task.setDescription("Task Description");
        task.setCompleted(true);

        Label label = new Label();
        label.setName("Urgent");
        label.setColor("#FF0000FF");
        task.getLabels().add(label);

        Task createdTask = taskController.createTask(task);
        assertNotNull(createdTask.getId());
        assertEquals("Task Description", createdTask.getDescription());
        assertTrue(createdTask.getCompleted());
        assertNotNull(createdTask.getTimestamp());
        assertEquals(1, createdTask.getLabels().size());
        assertEquals("#FF0000FF", createdTask.getLabels().get(0).getColor());
    }

    @Test
    void testDeleteLabelAssignedToTask() {
        Label label = new Label();
        label.setName("Work");
        label = labelRepository.save(label);

        Task task = new Task();
        task.setName("Do work");
        task.getLabels().add(label);
        task = taskController.createTask(task);

        Long labelId = label.getId();
        assertDoesNotThrow(() -> labelController.delte(labelId));

        Task refreshedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertTrue(refreshedTask.getLabels().isEmpty());
        assertFalse(labelRepository.existsById(labelId));
    }

    @Test
    void testGetNonExistentLabelThrowsException() {
        assertThrows(LabelNotFoundException.class, () -> labelController.getById(999L));
    }
}
