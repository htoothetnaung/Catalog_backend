package com.youtube.petAdoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.youtube.petAdoption.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
