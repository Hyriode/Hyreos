package fr.hyriode.hyreos;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyreos.config.HyreosConfig;
import fr.hyriode.hyreos.influxdb.InfluxDB;
import fr.hyriode.hyreos.logger.ColoredLogger;
import fr.hyriode.hyreos.metrics.PlayerRelatedHandler;

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

    private HyreosConfig config;
    private InfluxDB influxDB;

    private PlayerRelatedHandler playerHandler;

    public void start() {
        instance = this;

        ColoredLogger.printHeaderMessage();

        this.logger = new ColoredLogger("Hyreos", Paths.get("latest.log"));

        this.config = HyreosConfig.load();
        this.influxDB = new InfluxDB(this.config.getInfluxConfig());

        this.playerHandler = new PlayerRelatedHandler();
        this.playerHandler.start();

        HyriAPI.get().getScheduler().schedule(this.playerHandler::start, 60, 60, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        System.out.println("Stopping " + Hyreos.NAME + "...");

        this.influxDB.stop();
    }

    public static Hyreos get() {
        return instance;
    }

    public HyreosConfig getConfig() {
        return this.config;
    }

    public InfluxDB getInfluxDB() {
        return this.influxDB;
    }

    public ColoredLogger getLogger() {
        return this.logger;
    }
}
