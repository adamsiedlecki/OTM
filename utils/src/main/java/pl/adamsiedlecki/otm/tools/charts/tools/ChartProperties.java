package pl.adamsiedlecki.otm.tools.charts.tools;

public enum ChartProperties {

    TEMPERATURE_DEFAULT(" OTM Adam Siedlecki    Wykres temperatury "),
    VOLTAGE_DEFAULT(" OTM Adam Siedlecki    Wykres napięcia "),
    OPEN_WEATHER_FORECAST(" OTM Adam Siedlecki - Prognoza z Open Weather na najbliższy czas: "),
    TEMPERATURE_AXIS_TITLE("Temperatura °C");

    private final String title;

    ChartProperties(String s) {
        this.title = s;
    }

    public String get() {
        return title;
    }
}
