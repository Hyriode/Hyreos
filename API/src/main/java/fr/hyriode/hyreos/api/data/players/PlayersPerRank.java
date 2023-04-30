package fr.hyriode.hyreos.api.data.players;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.hyreos.api.data.IHyreosMetric;

import java.time.Instant;

@Measurement(name = "players_per_rank")
public class PlayersPerRank implements IHyreosMetric {

    @Column(name = "rank_name", tag = true)
    private final PlayerRank rank;
    @Column(name = "players")
    private final int players;
    @Column(timestamp = true)
    private Instant time;

    public PlayersPerRank(PlayerRank rank, int players) {
        this.rank = rank;
        this.players = players;
    }

    public PlayerRank getRank() {
        return this.rank;
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
