package fr.hyriode.hyreos.api.players;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.hyreos.api.IHyreosMetric;

import java.time.Instant;

@Measurement(name = "registered_players")
public class RegisteredPlayers implements IHyreosMetric {

    @Column(name = "players")
    private final long players;
    @Column(timestamp = true)
    private final Instant time;

    public RegisteredPlayers(long players) {
        this.players = players;

        this.time = Instant.now();
    }
}
