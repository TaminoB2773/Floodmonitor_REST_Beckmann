package service;

import model.Measurement;
import model.Station;
import model.WarningLevel;
import org.springframework.stereotype.Service;

@Service
public class FloodService {
    public WarningLevel calculateWarningLevel(Station station, Double waterLevel) {
        if (station == null || waterLevel == null || waterLevel < 0) {
            return WarningLevel.UNKNOWN;
        }

        if (waterLevel >= station.getCriticalWaterLevel()) {
            return WarningLevel.CRITICAL;
        } else if (waterLevel >= station.getWarningWaterLevel()) {
            return WarningLevel.WARNING;
        } else {
            return WarningLevel.NORMAL;
        }
    }
}