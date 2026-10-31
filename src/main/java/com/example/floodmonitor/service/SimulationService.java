package com.example.floodmonitor.service;

import com.example.floodmonitor.model.Station;
import com.example.floodmonitor.model.StationStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class SimulationService {

    private List<Station> stations = new ArrayList<>();

    private static final String[] STATION_NAMES = {
            "Station Vienna", "Station Linz", "Station Salzburg",
            "Station Graz", "Station Innsbruck"
    };

    private final Random random = new Random();

    @PostConstruct
    public void initStations() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        for (int i = 0; i < STATION_NAMES.length; i++) {
            double waterLevel = Math.round((0.5 + random.nextDouble() * 6.0) * 100.0) / 100.0;
            double waterTemp = Math.round((4.0 + random.nextDouble() * 16.0) * 10.0) / 10.0;
            double niederschlag = 0.0;
            double battery = 100.0;

            // Übergabe exakt passend zu deinem Station-Konstruktor
            Station station = new Station(
                    String.valueOf(i + 1),
                    STATION_NAMES[i],
                    timestamp,
                    waterLevel,
                    waterTemp,
                    "m",
                    niederschlag,
                    battery
            );

            double startFlowRate = Math.pow(station.getWaterLevel(), 1.5) * 10;
            station.setFlowRate(Math.round(startFlowRate * 10.0) / 10.0);

            stations.add(station);
        }
    }

    public List<Station> getAllStations() {
        return this.stations;
    }

    public Optional<Station> getStationById(String id) {
        return this.stations.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    @Scheduled(fixedRateString = "${simulation.interval:10000}")
    public void generateNewMeasurements() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        for (Station station : stations) {
            if (station.getStas() == StationStatus.MAINTENANCE || station.getStas() == StationStatus.UNKNOWN) {
                continue;
            }

            double alterWasserstand = station.getWaterLevel();
            double alterNiederschlag = station.getNiederschlag();

            double abfluss = 0.05;
            double regenEffekt = alterNiederschlag * 0.1;
            double zufallsSchwankung = (random.nextDouble() * 0.1) - 0.05;

            station.setWaterLevel(alterWasserstand - abfluss + regenEffekt + zufallsSchwankung);

            double neuerNiederschlag = random.nextDouble() < 0.2 ? random.nextDouble() * 20.0 : 0.0;
            station.setNiederschlag(Math.round(neuerNiederschlag * 10.0) / 10.0);

            double neueFlowRate = Math.pow(station.getWaterLevel(), 1.5) * 10;
            station.setFlowRate(Math.round(neueFlowRate * 10.0) / 10.0);

            double alterAkku = station.getBatteryLevel();
            station.setBatteryLevel(alterAkku - 0.5);
            station.setTimestamp(timestamp);
        }
    }
}