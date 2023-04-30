package fr.hyriode.hyreos;

import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hyreos.api.protocol.packet.HyreosResponsePacket;
import fr.hyriode.hyreos.config.HyreosConfig;
import fr.hyriode.hyreos.influxdb.InfluxDB;
import fr.hyriode.hyreos.logger.ColoredLogger;
import fr.hyriode.hyreos.receiver.HyreosResponseReceiver;
import fr.hyriode.hyreos.redis.Redis;

import java.nio.file.Paths;

/**
 * Created by AstFaster
 * on 13/10/2022 at 20:48
 */
public class Hyreos {

    private static Hyreos instance;

    private ColoredLogger logger;

    private HyreosConfig config;
    private Redis redis;
    private InfluxDB influxDB;
    private HyreosAPI hyreosAPI;

    public void start() {
        instance = this;

        ColoredLogger.printHeaderMessage();

        this.logger = new ColoredLogger("Hyreos", Paths.get("latest.log"));

        System.out.println("Starting " + HyreosAPI.NAME + "...");

        this.config = HyreosConfig.load();
        this.redis = new Redis(this.config.getRedisConfig());
        this.influxDB = new InfluxDB(this.config.getInfluxConfig());
        this.hyreosAPI = new HyreosAPI();
        this.hyreosAPI.start(this.redis.getPool());

        Hyreos.get().getAPI().getMessaging().registerReceiver(HyreosResponsePacket.class, new HyreosResponseReceiver());

        this.hyreosAPI.getMetricsManager().start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        System.out.println("Stopping " + HyreosAPI.NAME + "...");

        this.hyreosAPI.stop();
        this.redis.stop();
        this.influxDB.stop();
    }

    public static Hyreos get() {
        return instance;
    }

    public HyreosAPI getAPI() {
        return this.hyreosAPI;
    }

    public HyreosConfig getConfig() {
        return this.config;
    }

    public Redis getRedis() {
        return this.redis;
    }

    public InfluxDB getInfluxDB() {
        return this.influxDB;
    }

    public ColoredLogger getLogger() {
        return this.logger;
    }

}
