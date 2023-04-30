package fr.hyriode.hyreos.api.data.players;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.hyreos.api.data.IHyreosMetric;

import java.time.Instant;

@Measurement(name = "hyriplus_players")
public class HyriPlusPlayers implements IHyreosMetric {

    @Column(name = "players")
    private final long players;
    @Column(timestamp = true)
    private Instant time;

    public HyriPlusPlayers(long players) {
        this.players = players;
    }

    public long getPlayers() {
        return this.players;
    }

    public Instant getTime() {
        return this.time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
