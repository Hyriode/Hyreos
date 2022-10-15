package fr.hyriode.hyreos.api.metrics;

import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hyreos.api.protocol.packet.HyreosMetricPacket;

/**
 * Created by AstFaster
 * on 15/10/2022 at 10:14
 */
public class HyreosMetricsManager {

    /** The {@link HyreosAPI} instance */
    private final HyreosAPI hyreosAPI;

    /**
     * Constructor of {@link HyreosMetricsManager}
     *
     * @param hyreosAPI The {@link HyreosAPI} instance
     */
    public HyreosMetricsManager(HyreosAPI hyreosAPI) {
        this.hyreosAPI = hyreosAPI;
    }

    /**
     * Write a {@linkplain HyreosMetric metric}
     *
     * @param metric The metric to write
     */
    public void writeMetric(HyreosMetric metric) {
        this.hyreosAPI.getMessaging().sendPacket(new HyreosMetricPacket(metric));
    }

}
