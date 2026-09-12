package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task name must not be blank")
    @Column(nullable = false)
    private String name;

    private String description;

    private Boolean completed = false;

    private Instant timestamp;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "task_labels", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<Label> labels = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void ensureDefaults() {
        if (this.completed == null) {
            this.completed = false;
        }
        if (this.timestamp == null) {
            this.timestamp = Instant.now();
        }
    }

    public Boolean getCompleted() {
        return completed != null ? completed : false;
    }

    public Instant getTimestamp() {
        return timestamp != null ? timestamp : Instant.now();
    }

}
