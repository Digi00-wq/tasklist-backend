package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String color;
    private List<ProjectTabDTO> tabs = new ArrayList<>();
    private List<LabelDTO> labels = new ArrayList<>();
    private List<TaskDTO> tasks = new ArrayList<>();
}
