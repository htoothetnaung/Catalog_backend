package com.youtube.petAdoption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.youtube.petAdoption.service.HistoryService;
import com.youtube.petAdoption.model.History;

import java.util.List;

@RestController
@RequestMapping("/api/adoptionHistory")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/getAll")
    public ResponseEntity<List<History>> getAllHistory() {
        return ResponseEntity.ok(historyService.getAllHistory());
    }
}
