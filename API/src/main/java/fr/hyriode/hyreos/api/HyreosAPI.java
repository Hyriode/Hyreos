package fr.hyriode.hyreos.api;

import fr.hyriode.hyreos.api.metrics.HyreosMetricsManager;
import fr.hyriode.hyreos.api.protocol.HyreosMessaging;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.function.Consumer;

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

    /** The required {@link JedisPool} to use {@link redis.clients.jedis.JedisPubSub} system */
    private final JedisPool jedisPool;

    /**
     * Main constructor of {@link HyreosAPI}
     *
     * @param jedisPool The required {@link JedisPool}
     */
    public HyreosAPI(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * Start the API internal systems
     */
    public void start() {
        System.out.println("Starting " + NAME + " API...");

        this.messaging = new HyreosMessaging(this);
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
     * Query a {@linkplain Jedis Redis resource} to perform action
     *
     * @param jedisConsumer The consumer that will be triggered after accessing to the {@linkplain Jedis resource}
     */
    public void redisProcess(Consumer<Jedis> jedisConsumer) {
        try (final Jedis jedis = this.jedisPool.getResource()) {
            if (jedis != null) {
                jedisConsumer.accept(jedis);
            }
        }
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
