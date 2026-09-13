package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_tabs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTab {

    @Id
    private String id;

    private String name;
    private String subtitle;
    private String color;
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}
