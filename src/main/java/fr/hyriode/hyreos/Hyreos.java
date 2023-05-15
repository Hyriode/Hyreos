package fr.hyriode.hyreos;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.application.HyriAPIImpl;
import fr.hyriode.api.impl.application.config.HyriAPIConfig;
import fr.hyriode.hyreos.config.HyreosConfig;
import fr.hyriode.hyreos.influx.InfluxDB;
import fr.hyriode.hyreos.metrics.MetricsManager;
import fr.hyriode.hyreos.util.logger.ColoredLogger;

import java.nio.file.Paths;

/**
 * Created by AstFaster
 * on 13/10/2022 at 20:48
 */
public class Hyreos {

    public static final String NAME = "Hyreos";
    private static Hyreos instance;

    private ColoredLogger logger;
    private InfluxDB influx;
    private MetricsManager metrics;

    public void start() {
        instance = this;

        ColoredLogger.printHeaderMessage();
        this.logger = new ColoredLogger("Hyreos", Paths.get("latest.log"));

        final HyreosConfig config = HyreosConfig.load();
        new HyriAPIImpl(new HyriAPIConfig.Builder()
                .withRedisConfig(config.redis())
                .withMongoDBConfig(config.mongo())
                .withDevEnvironment(false)
                .withHyggdrasil(true)
                .build(), Hyreos.NAME);

        this.influx = new InfluxDB(config.influx());

        System.out.println("Setting up metrics handler...");
        this.metrics = new MetricsManager();
        this.metrics.start();

        HyriAPI.get().getScheduler().runAsync(() -> this.metrics.initialize());
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        System.out.println("Stopping " + Hyreos.NAME + "...");

        this.influx.stop();
    }

    public static Hyreos get() {
        return instance;
    }

    public ColoredLogger getLogger() {
        return this.logger;
    }

    public InfluxDB getInflux() {
        return this.influx;
    }

    public MetricsManager getMetrics() {
        return this.metrics;
    }
}
