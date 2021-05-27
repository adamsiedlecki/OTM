package pl.adamsiedlecki.OTM.externalServices.openWeather.pojo.openWeatherTwoDaysAhead;

import java.util.List;

public class OpenWeatherTwoDaysAheadPojo {
    private double lat;
    private double lon;
    private String timezone;
    private int timezone_offset;
    private List<Hourly> hourly;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(int timezone_offset) {
        this.timezone_offset = timezone_offset;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }

    public void setHourly(List<Hourly> hourly) {
        this.hourly = hourly;
    }
}
