package fr.hyriode.hyreos.api.data.players;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.hyreos.api.data.IHyreosMetric;

import java.time.Instant;

@Measurement(name = "connected_players")
public class ConnectedPlayers implements IHyreosMetric {

    @Column(name = "players")
    private final long players;
    @Column(timestamp = true)
    private Instant time;

    public ConnectedPlayers(long players) {
        this.players = players;

        this.time = Instant.now();
    }
}
