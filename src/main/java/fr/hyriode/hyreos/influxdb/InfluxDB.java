package fr.hyriode.hyreos.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyreos.Hyreos;
import fr.hyriode.hyreos.api.IHyreosMetric;
import fr.hyriode.hyreos.config.nested.InfluxConfig;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by AstFaster
 * on 14/10/2022 at 19:40
 */
public class InfluxDB {

    private final WriteApiBlocking writeApi;
    private final InfluxDBClient client;

    public InfluxDB(InfluxConfig config) {
        this.client = InfluxDBClientFactory.create(config.getUrl(), config.getToken().toCharArray(), config.getOrganization(), config.getBucket());

        try {
            if (this.client.ping()) {
                System.out.println("Connection set between " + Hyreos.NAME + " and InfluxDB");
            } else {
                System.err.println("Couldn't connect to InfluxDB");
            }
        } catch (Exception ignored) {
            System.exit(-1);
        }

        this.writeApi = this.client.getWriteApiBlocking();
    }

    public void write(Consumer<WriteApiBlocking> consumer) {
        HyriAPI.get().getScheduler().runAsync(() -> consumer.accept(this.writeApi));
    }

    public void sendMetrics(IHyreosMetric metric) {
        this.write(api -> api.writeMeasurement(WritePrecision.MS, metric));
    }

    public void sendMetrics(Set<IHyreosMetric> metrics) {
        for (final IHyreosMetric metric : metrics) {
            this.sendMetrics(metric);
        }
    }

    public void stop() {
        this.client.close();
    }
}
