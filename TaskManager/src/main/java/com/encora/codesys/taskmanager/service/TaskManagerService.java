package com.encora.codesys.taskmanager.service;

import com.encora.codesys.taskmanager.entity.Task;
import com.encora.codesys.taskmanager.repository.TaskManagerRepository;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for managing tasks.
 */
@Service
public class TaskManagerService {

    @Autowired
    private TaskManagerRepository taskRepository; // Injects the TaskManagerRepository for database operations

    /**
     * Retrieves all tasks.
     *
     * @return A list of all tasks.
     */
    public List<Task> getAllTask() {
        return (List<Task>) taskRepository.findAll();
    }

    /**
     * Retrieves a task by its title.
     *
     * @param title The title of the task to retrieve.
     * @return The task with the specified title.
     */
    public Task getTaskByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The task with the specified ID.
     * @throws RuntimeException If the task is not found.
     */
    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException("Task not found"));
    }

    /**
     * Creates a new task.
     *
     * @param task The task object to create.
     * @return The created task.
     */
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Updates an existing task.
     *
     * @param updatedTask The task object with updated details.
     * @return The updated task.
     * @throws IllegalArgumentException If the task ID is null.
     */
    public Task updateTask(Task updatedTask) {
        if (updatedTask.getId() == null) {
            throw new IllegalArgumentException("Task ID cannot be null for update operation");
        }
        return taskRepository.save(updatedTask);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     */
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * Deletes a task.
     *
     * @param task The task object to delete.
     */
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    /**
     * Private method to generate sample test data.
     *
     * @return A set of sample tasks.
     */
    private Set<Task> testData() {
        return Collections.asSet(
                Arrays.asList(
                        new Task(1L, "T1", "Pending", "test", new Date()),
                        new Task(2L, "T2", "Pending", "test", new Date()),
                        new Task(3L, "T3", "Pending", "test", new Date())
                )
        );
    }

}
