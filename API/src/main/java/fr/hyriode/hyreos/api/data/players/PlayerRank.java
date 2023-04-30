package fr.hyriode.hyreos.api.data.players;

public enum PlayerRank {

    PLAYER("player"),
    VIP("vip"),
    VIP_PLUS("vip+"),
    EPIC("epic"),

    /** Content creator */
    PARTNER("partner");

    private final String name;

    PlayerRank(String name) {
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
