package com.example.floodmonitor.controller;

import com.example.floodmonitor.exception.ResourceNotFoundException;
import com.example.floodmonitor.model.Station;
import com.example.floodmonitor.model.StationStatus;
import com.example.floodmonitor.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stations")
public class StationController {

    private final SimulationService simulationService;

    public StationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    // 1. Alle Stationen abrufen (mit optionalem Filter für warningLevel)
    @GetMapping
    public ResponseEntity<List<Station>> getStations(
            @RequestParam(required = false) String river,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) StationStatus warningLevel) {

        List<Station> result = simulationService.getAllStations().stream()
                .filter(s -> warningLevel == null || s.getStas() == warningLevel) // Falls deine Getter-Methode anders heißt, hier anpassen (z.B. getStatus())
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // 2. Einzelne Station abrufen
    @GetMapping("/{stationId}")
    public ResponseEntity<Station> getStationById(@PathVariable String stationId) {
        Station station = simulationService.getAllStations().stream()
                .filter(s -> s.getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Station mit der ID " + stationId + " wurde nicht gefunden"));

        return ResponseEntity.ok(station);
    }

    // 3. Aktuellsten Messwert einer Station abrufen
    @GetMapping("/{stationId}/measurements/latest")
    public ResponseEntity<Station> getLatestMeasurement(@PathVariable String stationId) {
        Optional<Station> stationOpt = simulationService.getAllStations().stream()
                .filter(s -> s.getId().equals(stationId))
                .findFirst();

        if (stationOpt.isPresent()) {
            return ResponseEntity.ok(stationOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}