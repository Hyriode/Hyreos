package fr.hyriode.hyreos.api.data.players;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.hyreos.api.data.IHyreosMetric;
import fr.hyriode.hyreos.api.data.service.ServiceType;

import java.time.Instant;

@Measurement(name = "players_per_service")
public class PlayersPerService implements IHyreosMetric {

    @Column(name = "service_type", tag = true)
    private final ServiceType type;
    @Column(name = "service_name", tag = true)
    private final String name;
    @Column(name = "players")
    private final int players;
    @Column(timestamp = true)
    private Instant time;

    public PlayersPerService(ServiceType type, String name, int players) {
        this.type = type;
        this.name = name;
        this.players = players;
    }

    public ServiceType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
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
