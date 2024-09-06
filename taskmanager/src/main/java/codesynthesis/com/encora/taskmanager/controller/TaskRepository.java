package codesynthesis.com.encora.taskmanager.controller;


import org.springframework.data.mongodb.repository.MongoRepository;

import codesynthesis.com.encora.taskmanager.dto.Task;

public interface TaskRepository extends MongoRepository<Task, String> {
    // You can add custom query methods here if needed
}
