package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "labels")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Pattern(regexp = "^#([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6})$", message = "Invalid HEX color format")
    @Column(length = 9, nullable = true)
    private String color;

}
