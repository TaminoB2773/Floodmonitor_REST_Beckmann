package repository;

import model.Location;
import model.Measurement;
import model.Station;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StationRepository {

    private final Map<String, Station> stations = new HashMap<>();
    private final Map<String, List<Measurement>> measurements = new HashMap<>();

    public StationRepository() {
        // Initial 3 Stationen anlegen (Anforderung: mind. 3 Stationen)
        Station s1 = new Station("ST-001", "Reichsbrücke", "Donau", new Location(48.2251, 16.4083), 250.0, 450.0, 600.0, true);
        Station s2 = new Station("ST-002", "Korneuburg", "Donau", new Location(48.3444, 16.3333), 220.0, 400.0, 550.0, true);
        Station s3 = new Station("ST-003", "Innsbruck Süd", "Inn", new Location(47.2627, 11.3945), 180.0, 300.0, 420.0, true);

        saveStation(s1);
        saveStation(s2);
        saveStation(s3);
    }

    public List<Station> findAllStations() {
        return new ArrayList<>(stations.values());
    }

    public Optional<Station> findStationById(String id) {
        return Optional.ofNullable(stations.get(id));
    }

    public Station saveStation(Station station) {
        stations.put(station.getId(), station);
        measurements.putIfAbsent(station.getId(), new ArrayList<>());
        return station;
    }

    public void addMeasurement(String stationId, Measurement measurement) {
        if (measurements.containsKey(stationId)) {
            measurements.get(stationId).add(measurement);
        }
    }

    public List<Measurement> findMeasurementsByStationId(String stationId) {
        return measurements.getOrDefault(stationId, Collections.emptyList());
    }
}