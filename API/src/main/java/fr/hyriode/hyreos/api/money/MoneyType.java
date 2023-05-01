package fr.hyriode.hyreos.api.money;

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
}
