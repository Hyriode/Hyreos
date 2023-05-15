package fr.hyriode.hyreos.metrics.handler;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hylios.api.MetricsRedisKey;
import fr.hyriode.hyreos.Hyreos;
import fr.hyriode.hyreos.metrics.data.IHyreosMetric;
import fr.hyriode.hyreos.metrics.data.money.CirculatingMoney;
import fr.hyriode.hyreos.metrics.data.money.MoneyType;
import fr.hyriode.hyreos.metrics.processor.IMetricHandler;
import fr.hyriode.hyreos.metrics.processor.IMetricProcessor;
import fr.hyriode.hyreos.util.Fetcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoneyMetricsHandler implements IMetricHandler {

    private static final IMetricProcessor HYRIS = () -> {
        final long hyris = Fetcher.fetch(MetricsRedisKey.HYRIS);

        return Set.of(new CirculatingMoney(MoneyType.HYRIS, hyris));
    };
    private static final IMetricProcessor HYODES = () -> {
        final long hyodes = Fetcher.fetch(MetricsRedisKey.HYODES);

        return Set.of(new CirculatingMoney(MoneyType.HYODES, hyodes));
    };

    @Override
    public boolean isInitialized() {
        final boolean hyris = Fetcher.exists(MetricsRedisKey.HYRIS);
        final boolean hyodes = Fetcher.exists(MetricsRedisKey.HYODES);

        return hyris && hyodes;
    }

    @Override
    public void initialize(List<IHyriPlayer> players) {
        Hyreos.get().getLogger().info("Initializing money metrics...");
        long hyris = 0;
        long hyodes = 0;

        for (final IHyriPlayer player : players) {
            hyris += player.getHyris().getAmount();
            hyodes += player.getHyodes().getAmount();
        }

        Fetcher.update(MetricsRedisKey.HYRIS, hyris);
        Fetcher.update(MetricsRedisKey.HYODES, hyodes);
    }

    @Override
    public Set<IHyreosMetric> process() {
        final Set<IHyreosMetric> metrics = new HashSet<>();

        metrics.addAll(HYRIS.process());
        metrics.addAll(HYODES.process());

        return metrics;
    }
}
