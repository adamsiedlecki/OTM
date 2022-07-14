package pl.adamsiedlecki.otm.odg.properties;

public enum ChartProperties {

    TEMPERATURE_DEFAULT(" OTM Adam Siedlecki    Wykres temperatury "),
    VOLTAGE_DEFAULT(" OTM Adam Siedlecki    Wykres napięcia "),
    OPEN_WEATHER_FORECAST(" OTM Adam Siedlecki - Prognoza z Open Weather na najbliższy czas: "),
    TEMPERATURE_AXIS_TITLE("Temperatura °C"),
    TIME_AXIS_TITLE("Czas");

    private final String title;

    ChartProperties(String s) {
        this.title = s;
    }

    public String get() {
        return title;
    }
}