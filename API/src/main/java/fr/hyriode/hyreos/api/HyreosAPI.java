package fr.hyriode.hyreos.api;

import fr.hyriode.hyreos.api.metrics.HyreosMetricsManager;
import fr.hyriode.hyreos.api.protocol.HyreosMessaging;
import redis.clients.jedis.JedisPool;

/**
 * Created by AstFaster
 * on 14/10/2022 at 18:58
 */
public class HyreosAPI {

    /** The name of the application */
    public static final String NAME = "Hyreos";

    /** The {@linkplain HyreosMessaging messaging} system */
    private HyreosMessaging messaging;
    /** The {@linkplain HyreosMetricsManager metrics' manager} */
    private HyreosMetricsManager metricsManager;

    /**
     * Start the API internal systems
     */
    public void start(JedisPool pool) {
        System.out.println("Starting " + NAME + " API...");

        this.messaging = new HyreosMessaging(this, pool);
        this.metricsManager = new HyreosMetricsManager(this);
    }

    /**
     * Stop the API internal systems
     */
    public void stop() {
        System.out.println("Stopping " + NAME + " API...");

        this.messaging.stop();
    }

    /**
     * Get the {@linkplain HyreosMessaging messaging} system instance
     *
     * @return The {@link HyreosMessaging} instance
     */
    public HyreosMessaging getMessaging() {
        return this.messaging;
    }

    /**
     * Get the {@linkplain HyreosMetricsManager metrics manager} instance
     *
     * @return The {@link HyreosMetricsManager} instance
     */
    public HyreosMetricsManager getMetricsManager() {
        return this.metricsManager;
    }

}
