package fr.hyriode.hyreos.api.protocol.request.money;

import fr.hyriode.hyreos.api.data.money.MoneyType;
import fr.hyriode.hyreos.api.protocol.packet.HyreosPacket;

public class EmittedCurrenciesRequest implements HyreosPacket {

    private final MoneyType type;

    public EmittedCurrenciesRequest(MoneyType type) {
        this.type = type;
    }

    public MoneyType getType() {
        return this.type;
    }
}
