package fr.hyriode.hyreos.api.metrics;

import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hyreos.api.protocol.HyreosMessaging;
import fr.hyriode.hyreos.api.data.service.ServiceType;
import fr.hyriode.hyreos.api.protocol.request.players.PlayersPerServiceRequest;

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

            messaging.sendPacket(new PlayersPerServiceRequest(ServiceType.LIMBO));
            messaging.sendPacket(new PlayersPerServiceRequest(ServiceType.PROXY));
            messaging.sendPacket(new PlayersPerServiceRequest(ServiceType.SERVER));

            //messaging.sendPacket(new PlayersPerGameRequest());
        }, 60, 60, TimeUnit.SECONDS);
    }
}
