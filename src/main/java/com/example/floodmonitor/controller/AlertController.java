package com.example.floodmonitor.controller;

import com.example.floodmonitor.model.Station;
import com.example.floodmonitor.model.StationStatus;
import com.example.floodmonitor.service.SimulationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/alerts") // Basispfad für die Warnungen
public class AlertController {

    private final SimulationService simulationService;

    public AlertController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }
    @GetMapping
    public List<Station> getActiveAlerts() {
        // Holt die Liste aller Stationen und filtert die kritischen heraus
        return simulationService.getAllStations().stream()
                .filter(station -> station.getStas() == StationStatus.WARNING || station.getStas() == StationStatus.CRITICAL)
                .collect(Collectors.toList());
    }
}