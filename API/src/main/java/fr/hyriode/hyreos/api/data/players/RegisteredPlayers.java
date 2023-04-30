package fr.hyriode.hyreos.api.data.players;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.hyreos.api.data.IHyreosMetric;

import java.time.Instant;

@Measurement(name = "registered_players")
public class RegisteredPlayers implements IHyreosMetric {

    @Column(name = "players")
    private final long players;
    @Column(timestamp = true)
    private Instant time;

    public RegisteredPlayers(long players) {
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
