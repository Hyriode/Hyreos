package fr.hyriode.hyreos.receiver;

import com.influxdb.client.domain.WritePrecision;
import fr.hyriode.hyreos.Hyreos;
import fr.hyriode.hyreos.api.data.IHyreosMetric;
import fr.hyriode.hyreos.api.protocol.HyreosReceiver;
import fr.hyriode.hyreos.api.protocol.packet.HyreosResponsePacket;

public class HyreosResponseReceiver implements HyreosReceiver<HyreosResponsePacket> {

    @Override
    public void onReceive(HyreosResponsePacket packet) {
        Hyreos.get().getInfluxDB().write(api -> {
            for (final IHyreosMetric response : packet.getResponses()) {
                api.writeMeasurement(WritePrecision.MS, response);
            }
        });
    }
}
