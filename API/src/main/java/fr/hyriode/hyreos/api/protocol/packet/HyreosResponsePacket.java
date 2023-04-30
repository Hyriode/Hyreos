package fr.hyriode.hyreos.api.protocol.packet;

import fr.hyriode.hyreos.api.data.IHyreosMetric;

import java.util.Set;

public class HyreosResponsePacket implements HyreosPacket {

    private final Set<IHyreosMetric> responses;

    public HyreosResponsePacket(Set<IHyreosMetric> responses) {
        this.responses = responses;
    }

    public Set<IHyreosMetric> getResponses() {
        return this.responses;
    }
}
