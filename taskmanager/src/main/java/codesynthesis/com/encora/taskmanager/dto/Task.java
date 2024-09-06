package codesynthesis.com.encora.taskmanager.dto;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

@Document("Tasks") // This will be the collection name in MongoDB
public class Task {

    @Id
    private String id;
    private String name;
    private String description;
    private Date dueDate;
    private Date completionDate;
    private boolean isComplete;

    // Constructors
    public Task() {
        // Default constructor for Spring Data MongoDB
    }

    public Task(String id, String name, String description, Date dueDate, Date completionDate, boolean isComplete) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
        this.isComplete = isComplete;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    // hashCode and equals methods for proper object comparison
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, dueDate, completionDate, isComplete);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(description, other.description) && Objects.equals(dueDate, other.dueDate)
                && Objects.equals(completionDate, other.completionDate) && isComplete == other.isComplete;
    }

    // toString method for easy object representation
    @Override
    public String toString() {
        return "Task [id=" + id + ", name=" + name + ", description=" + description + ", dueDate=" + dueDate
                + ", completionDate=" + completionDate + ", isComplete=" + isComplete + "]";
    }
}
