package fr.hyriode.hyreos;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.application.HyriAPIImpl;
import fr.hyriode.api.impl.application.config.HyriAPIConfig;
import fr.hyriode.hyreos.config.HyreosConfig;
import fr.hyriode.hyreos.influxdb.InfluxDB;
import fr.hyriode.hyreos.logger.ColoredLogger;
import fr.hyriode.hyreos.metrics.MetricsHandler;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Created by AstFaster
 * on 13/10/2022 at 20:48
 */
public class Hyreos {

    public static final String NAME = "Hyreos";
    private static Hyreos instance;

    private ColoredLogger logger;
    private InfluxDB influxDB;

    public void start() {
        instance = this;

        ColoredLogger.printHeaderMessage();
        this.logger = new ColoredLogger("Hyreos", Paths.get("latest.log"));

        final HyreosConfig config = HyreosConfig.load();
        new HyriAPIImpl(new HyriAPIConfig.Builder()
                .withRedisConfig(config.getRedisConfig())
                .withMongoDBConfig(config.getMongoConfig())
                .withDevEnvironment(true)
                .withHyggdrasil(false)
                .build(), Hyreos.NAME);

        this.influxDB = new InfluxDB(config.getInfluxConfig());

        System.out.println("Setting up metrics handler...");
        final MetricsHandler handler = new MetricsHandler();
        handler.start();

        HyriAPI.get().getScheduler().schedule(handler::handle, 60, 60, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        System.out.println("Stopping " + Hyreos.NAME + "...");

        this.influxDB.stop();
    }

    public static Hyreos get() {
        return instance;
    }

    public InfluxDB getInfluxDB() {
        return this.influxDB;
    }

    public ColoredLogger getLogger() {
        return this.logger;
    }
}
