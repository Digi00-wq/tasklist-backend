package com.example.demo.repository;

import com.example.demo.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    List<Label> findByProjectId(Long projectId);

    @Modifying
    @Query(value = "DELETE FROM task_labels WHERE label_id = :labelId", nativeQuery = true)
    void deleteLabelAssociations(@Param("labelId") Long labelId);
}
