package pl.adamsiedlecki.otm.tools.charts.tools;

public enum ChartTitle {

    DEFAULT(" OTM Adam Siedlecki    Wykres temperatury "),
    OPEN_WEATHER_FORECAST(" OTM Adam Siedlecki - Prognoza z Open Weather na najbliższy czas: ");

    private final String title;

    ChartTitle(String s) {
        this.title = s;
    }

    public String get() {
        return title;
    }
}
