package pl.adamsiedlecki.otm.db.statistics;

public enum ExistingStatistics {

    REQUESTS_TO_ESP_COUNT("requestsToEspCount"),
    ESP_NO_RESPONSE_COUNT("espNoResponseCount"),
    ESP_RESTART_COUNT("espRestartCount"),
    FACEBOOK_POST_CHART("facebookPostChart"),
    FACEBOOK_POST_MESSAGE("facebookPostMessage"),
    FACEBOOK_POST_COMMENT("facebookPostComment");

    private final String key;

    ExistingStatistics(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }
}
