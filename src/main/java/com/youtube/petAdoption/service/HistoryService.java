package com.youtube.petAdoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.youtube.petAdoption.repository.HistoryRepository;
import com.youtube.petAdoption.model.History;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    // Fetch all adoption history records
    public List<History> getAllHistory() {
        return historyRepository.findAll();
    }
}
