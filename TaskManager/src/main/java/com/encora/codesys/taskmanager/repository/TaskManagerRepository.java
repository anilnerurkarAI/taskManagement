package com.encora.codesys.taskmanager.repository;

import com.encora.codesys.taskmanager.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Task entities in the database.
 */
@Repository // Marks this interface as a Spring Data repository
public interface TaskManagerRepository extends CrudRepository<Task, Long> {

    /**
     * Finds a task by its title.
     *
     * @param title The title of the task to search for.
     * @return The Task entity with the given title, or null if not found.
     */
    Task findByTitle(String title);
}
