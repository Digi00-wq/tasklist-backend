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
    void testCreateTaskWithNewLabelAndColor() {
        Task task = new Task();
        task.setName("Test Task");
        task.setDescription("Task Description");
        task.setCompleted(true);
        task.setTimestamp(Instant.now());

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
    void testOptionalFieldsBackwardCompatibility() {
        // Create task without setting optional fields (simulating legacy data)
        Task task = new Task();
        task.setName("Legacy Task");

        Task createdTask = taskController.createTask(task);
        assertNotNull(createdTask.getId());
        assertNull(createdTask.getDescription());
        assertNull(createdTask.getCompleted());
        assertNull(createdTask.getTimestamp());

        Label label = new Label();
        label.setName("Legacy Label");
        Label createdLabel = labelController.create(label);
        assertNull(createdLabel.getColor());
    }

    @Test
    void testCreateTaskWithExistingLabel() {
        Label existingLabel = new Label();
        existingLabel.setName("Existing");
        existingLabel.setColor("#00FF00FF");
        existingLabel = labelRepository.save(existingLabel);

        Task task = new Task();
        task.setName("Task With Existing Label");
        Label inputLabel = new Label();
        inputLabel.setId(existingLabel.getId());
        task.getLabels().add(inputLabel);

        Task createdTask = taskController.createTask(task);
        assertEquals(1, createdTask.getLabels().size());
        assertEquals(existingLabel.getId(), createdTask.getLabels().get(0).getId());
        assertEquals("Existing", createdTask.getLabels().get(0).getName());
        assertEquals("#00FF00FF", createdTask.getLabels().get(0).getColor());
    }

    @Test
    void testUpdateTaskLabelsAndFields() {
        Label label1 = new Label();
        label1.setName("Label 1");
        label1 = labelRepository.save(label1);

        Task task = new Task();
        task.setName("Original Task");
        task.getLabels().add(label1);
        task = taskController.createTask(task);

        Label label2 = new Label();
        label2.setName("Label 2");
        label2 = labelRepository.save(label2);

        Task updatePayload = new Task();
        updatePayload.setName("Updated Task");
        updatePayload.setDescription("New Description");
        updatePayload.setCompleted(true);
        updatePayload.setTimestamp(Instant.now());
        updatePayload.getLabels().add(label2);

        Task updatedTask = taskController.editTask(task.getId(), updatePayload);
        assertEquals("Updated Task", updatedTask.getName());
        assertEquals("New Description", updatedTask.getDescription());
        assertTrue(updatedTask.getCompleted());
        assertNotNull(updatedTask.getTimestamp());
        assertEquals(1, updatedTask.getLabels().size());
        assertEquals("Label 2", updatedTask.getLabels().get(0).getName());
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
