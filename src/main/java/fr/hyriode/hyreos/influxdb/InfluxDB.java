package fr.hyriode.hyreos.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hyreos.config.InfluxDBConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Created by AstFaster
 * on 14/10/2022 at 19:40
 */
public class InfluxDB {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final WriteApiBlocking writeApi;

    private final InfluxDBClient client;

    public InfluxDB(InfluxDBConfig config) {
        this.client = InfluxDBClientFactory.create(config.getUrl(), config.getToken().toCharArray(), config.getOrganization(), config.getBucket());

        try {
            if (this.client.ping()) {
                System.out.println("Connection set between " + HyreosAPI.NAME + " and InfluxDB.");
            } else {
                System.err.println("Couldn't connect to InfluxDB.");
            }
        } catch (Exception ignored) {
            System.exit(-1);
        }

        this.writeApi = this.client.getWriteApiBlocking();
    }

    public void write(Consumer<WriteApiBlocking> writeApiConsumer) {
        this.executor.execute(() -> writeApiConsumer.accept(writeApi));
    }

    public void stop() {
        this.executor.shutdown();
        this.client.close();
    }

}