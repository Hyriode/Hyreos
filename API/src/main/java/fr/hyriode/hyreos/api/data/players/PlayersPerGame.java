package fr.hyriode.hyreos.api.data.players;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.hyreos.api.data.IHyreosMetric;

import java.time.Instant;

@Measurement(name = "players_per_game")
public class PlayersPerGame implements IHyreosMetric {

    @Column(name = "game_name", tag = true)
    private final String game;
    @Column(name = "game_type", tag = true)
    private final String type;
    @Column(name = "players")
    private final int players;
    @Column(timestamp = true)
    private Instant time;

    public PlayersPerGame(String game, String type, int players) {
        this.game = game;
        this.type = type;
        this.players = players;
    }

    public String getGame() {
        return this.game;
    }

    public String getType() {
        return this.type;
    }

    public int getPlayers() {
        return this.players;
    }

    public Instant getTime() {
        return this.time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
