package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

}
