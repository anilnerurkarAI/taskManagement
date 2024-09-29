package com.encora.codesys.taskmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Represents a task in the task manager application.
 */
@Data // Lombok annotation to generate getters, setters, toString, etc.
@Entity // Marks this class as a JPA entity, representing a table in the database
@Table(name = "Task", indexes = {
        @Index(name = "idx_task_status", columnList = "status"), // Creates an index on the 'status' column for faster queries
        @Index(name = "idx_task_due_date", columnList = "dueDate") // Creates an index on the 'dueDate' column for faster queries
})
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
public class Task {

    @Id // Marks this field as the primary key of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies that the primary key is generated automatically as an incrementing value
    private Long id; // Unique identifier for the task

    @Column(nullable = false) // Specifies that the 'title' column cannot be null
    private String title; // Title of the task

    @Column(nullable = false) // Specifies that the 'status' column cannot be null
    private String status; // Status of the task (e.g., "In Progress", "Completed")

    private String description; // Description of the task

    @Column(nullable = false) // Specifies that the 'dueDate' column cannot be null
    private Date dueDate; // Due date of the task
}
