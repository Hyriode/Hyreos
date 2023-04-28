package fr.hyriode.hyreos.config;

import fr.hyriode.hyreos.config.nested.InfluxConfig;
import fr.hyriode.hyreos.config.nested.RedisConfig;
import fr.hyriode.hyreos.util.YamlLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by AstFaster
 * on 14/10/2022 at 19:28
 */
public class HyreosConfig {

    public static final Path CONFIG_FILE = Paths.get("config.yml");

    private final RedisConfig redis;
    private final InfluxConfig influx;

    public HyreosConfig(RedisConfig redis, InfluxConfig influx) {
        this.redis = redis;
        this.influx = influx;
    }

    public RedisConfig getRedisConfig() {
        return this.redis;
    }

    public InfluxConfig getInfluxDBConfig() {
        return this.influx;
    }

    public static HyreosConfig load() {
        System.out.println("Loading configuration...");

        if (Files.exists(CONFIG_FILE)) {
            return YamlLoader.load(CONFIG_FILE, HyreosConfig.class);
        } else {
            final HyreosConfig config = new HyreosConfig(new RedisConfig(), new InfluxConfig());

            YamlLoader.save(CONFIG_FILE, config);

            System.err.println("Please fill configuration file before continue!");
            System.exit(0);

            return config;
        }
    }
}
