package fr.hyriode.hyreos.metrics.data.players;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.hyreos.metrics.data.IHyreosMetric;

import java.time.Instant;

@Measurement(name = "players_per_rank")
public class PlayersPerRank implements IHyreosMetric {

    @Column(name = "rank_name", tag = true)
    private final PlayerRank rank;
    @Column(name = "players")
    private final long players;
    @Column(timestamp = true)
    private final Instant time;

    public PlayersPerRank(PlayerRank rank, long players) {
        this.rank = rank;
        this.players = players;

        this.time = Instant.now();
    }
}
