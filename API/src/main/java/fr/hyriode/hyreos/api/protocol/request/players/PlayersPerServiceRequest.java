package fr.hyriode.hyreos.api.protocol.request.players;

import fr.hyriode.hyreos.api.data.service.ServiceType;
import fr.hyriode.hyreos.api.protocol.packet.HyreosPacket;

public class PlayersPerServiceRequest implements HyreosPacket {

    private final ServiceType type;

    public PlayersPerServiceRequest(ServiceType type) {
        this.type = type;
    }

    public ServiceType getType() {
        return this.type;
    }
}
