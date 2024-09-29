package com.encora.codesys.taskmanager.controller;

import com.encora.codesys.taskmanager.entity.Task;
import com.encora.codesys.taskmanager.service.TaskManagerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing tasks.
 */
@RestController
@RequestMapping("/task-manager") // Base mapping for all endpoints in this controller
public class TaskManagerController {

    @Autowired
    private TaskManagerService service; // Injects the TaskManagerService for handling task-related operations

    /**
     * Retrieves all tasks.
     *
     * @return A list of all tasks.
     */
    @GetMapping("/tasks")
    public List<Task> getAllTask() {
        return service.getAllTask();
    }

    /**
     * Retrieves a task by its title.
     *
     * @param title The title of the task to retrieve.
     * @return The task with the specified title, or a 404 Not Found status if not found.
     */
    @GetMapping("/tasks/{title}")
    public ResponseEntity<Task> getTaskByTitle(String title) {
        return new ResponseEntity<>(service.getTaskByTitle(title), HttpStatus.OK);
    }

    /**
     * Creates a new task.
     *
     * @param task The task object to create.
     * @return The created task with a 201 Created status.
     */
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = service.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Updates an existing task.
     *
     * @param task The task object with updated details.
     * @return The updated task with a 200 OK status.
     */
    @PutMapping("/tasks")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        Task updatedTask = service.updateTask(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @return A 204 No Content status if the deletion is successful.
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes a task.
     *
     * @param task The task object to delete.
     * @return A 204 No Content status if the deletion is successful.
     */
    @DeleteMapping("/tasks")
    public ResponseEntity<Void> deleteTask(@RequestBody Task task) {
        service.deleteTask(task);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
