package fr.hyriode.hyreos.api;

public enum MetricsRedisKeys {

    HYRIS_COUNT("hyris_count"),
    HYODES_COUNT("hyodes_count");

    private final String key;

    MetricsRedisKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
