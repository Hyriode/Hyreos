package fr.hyriode.hyreos.receiver;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import fr.hyriode.hyreos.Hyreos;
import fr.hyriode.hyreos.api.metrics.HyreosMetric;
import fr.hyriode.hyreos.api.protocol.HyreosReceiver;
import fr.hyriode.hyreos.api.protocol.packet.HyreosMetricPacket;

/**
 * Created by AstFaster
 * on 14/10/2022 at 20:10
 */
public class MetricReceiver implements HyreosReceiver<HyreosMetricPacket> {

    @Override
    public void onReceive(HyreosMetricPacket packet) {
        final HyreosMetric metric = packet.getMetric();

        Hyreos.get().getInfluxDB().write(writeApi -> {
            final Point point = Point.measurement(metric.getName())
                    .addFields(metric.getFields())
                    .addTags(metric.getTags())
                    .time(System.currentTimeMillis(), WritePrecision.MS);

            writeApi.writePoint(point);
        });
    }

}
