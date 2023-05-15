package fr.hyriode.hyreos.metrics.handler;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyreos.Hyreos;
import fr.hyriode.hyreos.api.HyreosRedisKey;
import fr.hyriode.hyreos.metrics.data.IHyreosMetric;
import fr.hyriode.hyreos.metrics.data.network.AppType;
import fr.hyriode.hyreos.metrics.data.network.PacketsPerMinute;
import fr.hyriode.hyreos.metrics.processor.IMetricHandler;
import fr.hyriode.hyreos.metrics.processor.IMetricProcessor;
import fr.hyriode.hyreos.util.Fetcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NetworkMetricsHandler implements IMetricHandler {

    private static final IMetricProcessor HYRIAPI = () -> {
        final long packets = Fetcher.fetch(HyreosRedisKey.HYRIAPI_PACKETS);
        Fetcher.update(HyreosRedisKey.HYRIAPI_PACKETS, 0);

        return Set.of(new PacketsPerMinute(AppType.HYRIAPI, packets));
    };
    private static final IMetricProcessor HYGGDRASIL = () -> {
        final long packets = Fetcher.fetch(HyreosRedisKey.HYGGDRASIL_PACKETS);
        Fetcher.update(HyreosRedisKey.HYGGDRASIL_PACKETS, 0);

        return Set.of(new PacketsPerMinute(AppType.HYGGDRASIL, packets));
    };

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public void initialize(List<IHyriPlayer> players) {
        Hyreos.get().getLogger().info("Initializing network metrics...");

        Fetcher.update(HyreosRedisKey.HYRIAPI_PACKETS, 0);
        Fetcher.update(HyreosRedisKey.HYGGDRASIL_PACKETS, 0);
    }

    @Override
    public Set<IHyreosMetric> process() {
        final Set<IHyreosMetric> metrics = new HashSet<>();

        metrics.addAll(HYRIAPI.process());
        metrics.addAll(HYGGDRASIL.process());

        return metrics;
    }
}
