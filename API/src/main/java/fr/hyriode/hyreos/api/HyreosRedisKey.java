package fr.hyriode.hyreos.api;

public enum HyreosRedisKey {

    HYRIS("money:hyris"),
    HYODES("money:hyodes"),

    HYRI_PLUS("ranks:hyriplus"),

    REGISTERED_PLAYERS("players:registered");

    private final String key;

    HyreosRedisKey(String key) {
        this.key = "hyreos:metrics:" + key;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
