package fr.hyriode.hyreos;

import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hyreos.api.metrics.HyreosMetric;
import fr.hyriode.hyreos.api.protocol.packet.HyreosMetricPacket;
import fr.hyriode.hyreos.api.protocol.packet.HyreosPacket;
import fr.hyriode.hyreos.config.HyreosConfig;
import fr.hyriode.hyreos.influxdb.InfluxDB;
import fr.hyriode.hyreos.logger.ColoredLogger;
import fr.hyriode.hyreos.receiver.MetricReceiver;
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
        this.influxDB = new InfluxDB(this.config.getInfluxDBConfig());
        this.hyreosAPI = new HyreosAPI(this.redis.getPool());
        this.hyreosAPI.start();

        this.hyreosAPI.getMessaging().registerReceiver(HyreosMetricPacket.class, new MetricReceiver());

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
