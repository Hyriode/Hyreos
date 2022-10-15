package fr.hyriode.hyreos.api.protocol.packet;

import fr.hyriode.hyreos.api.metrics.HyreosMetric;

/**
 * Created by AstFaster
 * on 14/10/2022 at 20:08
 *
 * This class represents what a packet used to create a metric is
 */
public class HyreosMetricPacket implements HyreosPacket {

    /** The metric object to create */
    private final HyreosMetric metric;

    /**
     * Default constructor of a {@link HyreosMetricPacket}
     *
     * @param metric The metric to create on received
     */
    public HyreosMetricPacket(HyreosMetric metric) {
        this.metric = metric;
    }

    /**
     * Get the {@link HyreosMetric} wrapped by the packet
     *
     * @return A {@link HyreosMetric} object
     */
    public HyreosMetric getMetric() {
        return this.metric;
    }

}
