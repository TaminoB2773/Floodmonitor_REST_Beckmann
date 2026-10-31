package com.example.floodmonitor.model;

public class Station {
    private String id;
    private String stationName;
    private String timestamp;
    private double waterLevel;
    private double waterTemperature;
    private String unit;
    private double niederschlag;
    private double flowRate;
    private StationStatus stas;
    private double batteryLevel;

    public Station() {}

    public Station(String id, String stationName, String timestamp, double waterLevel,
                   double waterTemperature, String unit, double niederschlag, double batteryLevel) {
        this.id = id;
        this.stationName = stationName;
        this.timestamp = timestamp;
        this.unit = unit;
        this.niederschlag = niederschlag;
        this.batteryLevel = batteryLevel;
        setWaterLevel(waterLevel);
        setWaterTemperature(waterTemperature);
    }

    private void updateStatus() {
        if (this.batteryLevel <= 10) {
            this.stas = StationStatus.MAINTENANCE;
        } else if (this.waterLevel < 5) {
            this.stas = StationStatus.NORMAL;
        } else if (this.waterLevel >= 5 && this.waterLevel < 8) {
            this.stas = StationStatus.WARNING;
        } else if (this.waterLevel >= 8) {
            this.stas = StationStatus.CRITICAL;
        } else {
            this.stas = StationStatus.UNKNOWN;
        }
    }

    // Getter und Setter (damit der SimulationService keine Fehler mehr wirft)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public double getWaterLevel() { return waterLevel; }
    public void setWaterLevel(double waterLevel) {
        if (waterLevel >= 0) {
            this.waterLevel = waterLevel;
            updateStatus();
        }
    }

    public double getWaterTemperature() { return waterTemperature; }
    public void setWaterTemperature(double waterTemperature) {
        if (waterTemperature >= 0 && waterTemperature <= 100) {
            this.waterTemperature = waterTemperature;
        }
    }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public double getNiederschlag() { return niederschlag; }
    public void setNiederschlag(double niederschlag) {
        this.niederschlag = Math.max(0, niederschlag);
    }

    public double getFlowRate() { return flowRate; }
    public void setFlowRate(double flowRate) {
        if (flowRate >= 0) {
            this.flowRate = flowRate;
        }
    }

    public double getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(double batteryLevel) {
        if (batteryLevel >= 0 && batteryLevel <= 100) {
            this.batteryLevel = batteryLevel;
            updateStatus();
        }
    }

    public StationStatus getStas() { return stas; }
    public void setStas(StationStatus stas) { this.stas = stas; }
}