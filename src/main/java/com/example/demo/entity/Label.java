package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Label name must not be blank")
    @Column(nullable = false)
    private String name;

    @Pattern(regexp = "^#([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6})$", message = "Invalid HEX color format")
    @Column(length = 9)
    private String color = "#000000FF";

    @PrePersist
    @PreUpdate
    public void ensureDefaults() {
        if (this.color == null || this.color.isBlank()) {
            this.color = "#000000FF";
        }
    }

    public String getColor() {
        return color != null && !color.isBlank() ? color : "#000000FF";
    }

}
