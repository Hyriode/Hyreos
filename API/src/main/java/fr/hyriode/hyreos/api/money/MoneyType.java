package fr.hyriode.hyreos.api.data.money;

import fr.hyriode.api.money.IHyriMoneyManager;

public enum MoneyType {

    HYRIS("hyris"),
    HYODES("hyodes");

    private final String name;

    MoneyType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getRedisKey() {
        if (this == MoneyType.HYRIS) {
            return IHyriMoneyManager.HYRIS_REDIS_KEY;
        }

        return IHyriMoneyManager.HYODES_REDIS_KEY;
    }
}
