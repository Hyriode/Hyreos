package fr.hyriode.hyreos.api.metrics;

import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hyreos.api.data.money.MoneyType;
import fr.hyriode.hyreos.api.protocol.HyreosMessaging;
import fr.hyriode.hyreos.api.data.service.ServiceType;
import fr.hyriode.hyreos.api.protocol.request.money.EmittedCurrenciesRequest;
import fr.hyriode.hyreos.api.protocol.request.players.*;
import fr.hyriode.hyreos.api.protocol.request.service.ResourcesPerServiceRequest;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by AstFaster
 * on 15/10/2022 at 10:14
 */
public class HyreosMetricsManager {

    /** The {@link HyreosAPI} instance */
    private final HyreosAPI hyreosAPI;

    /**
     * Constructor of {@link HyreosMetricsManager}
     *
     * @param hyreosAPI The {@link HyreosAPI} instance
     */
    public HyreosMetricsManager(HyreosAPI hyreosAPI) {
        this.hyreosAPI = hyreosAPI;
    }

    public void start() {
        Executors.newScheduledThreadPool(8).scheduleAtFixedRate(() -> {
            final HyreosMessaging messaging = this.hyreosAPI.getMessaging();

            messaging.sendPacket(new EmittedCurrenciesRequest(MoneyType.HYRIS));
            messaging.sendPacket(new EmittedCurrenciesRequest(MoneyType.HYODES));

            messaging.sendPacket(new HyriPlusPlayersRequest());
            messaging.sendPacket(new ConnectedPlayersRequest());
            messaging.sendPacket(new RegisteredPlayersRequest());

            messaging.sendPacket(new PlayersPerGameRequest());
            messaging.sendPacket(new PlayersPerRankRequest());

            messaging.sendPacket(new PlayersPerServiceRequest(ServiceType.LIMBO));
            messaging.sendPacket(new PlayersPerServiceRequest(ServiceType.PROXY));
            messaging.sendPacket(new PlayersPerServiceRequest(ServiceType.SERVER));

            messaging.sendPacket(new ResourcesPerServiceRequest(ServiceType.LIMBO));
            messaging.sendPacket(new ResourcesPerServiceRequest(ServiceType.PROXY));
            messaging.sendPacket(new ResourcesPerServiceRequest(ServiceType.SERVER));
        }, 60, 60, TimeUnit.SECONDS);
    }
}
