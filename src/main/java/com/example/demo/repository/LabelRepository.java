package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    @Modifying
    @Query(value = "DELETE FROM task_labels WHERE label_id = :labelId", nativeQuery = true)
    void deleteLabelAssociations(@Param("labelId") Long labelId);

}
