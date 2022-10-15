package fr.hyriode.hyreos.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import fr.hyriode.hyreos.util.IOUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by AstFaster
 * on 14/10/2022 at 19:28
 */
public class HyreosConfig {

    @SerializedName("redis")
    private final RedisConfig redisConfig;
    @SerializedName("influxdb")
    private final InfluxDBConfig influxDBConfig;

    public HyreosConfig(RedisConfig redisConfig, InfluxDBConfig influxDBConfig) {
        this.redisConfig = redisConfig;
        this.influxDBConfig = influxDBConfig;
    }

    public HyreosConfig() {
        this(new RedisConfig("127.0.0.1", 6379, ""), new InfluxDBConfig("http://127.0.0.1:8086", "", "", ""));
    }

    public RedisConfig getRedisConfig() {
        return this.redisConfig;
    }

    public InfluxDBConfig getInfluxDBConfig() {
        return this.influxDBConfig;
    }

    public static HyreosConfig load() {
        System.out.println("Loading configuration...");

        final Path configFile = Paths.get("config.json");
        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        final String json = IOUtil.loadFile(configFile);

        if (!json.equals("")) {
            return gson.fromJson(json, HyreosConfig.class);
        } else {
            final HyreosConfig config = new HyreosConfig();

            IOUtil.save(configFile, gson.toJson(config));

            System.err.println("Please fill configuration file!");
            System.exit(0);

            return config;
        }
    }

}
