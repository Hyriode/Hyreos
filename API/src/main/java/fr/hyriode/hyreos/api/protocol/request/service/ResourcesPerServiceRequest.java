package fr.hyriode.hyreos.api.protocol.request.service;

import fr.hyriode.hyreos.api.data.service.ServiceType;
import fr.hyriode.hyreos.api.protocol.packet.HyreosPacket;

public class ResourcesPerServiceRequest implements HyreosPacket {

    private final ServiceType type;

    public ResourcesPerServiceRequest(ServiceType type) {
        this.type = type;
    }

    public ServiceType getType() {
        return this.type;
    }
}
