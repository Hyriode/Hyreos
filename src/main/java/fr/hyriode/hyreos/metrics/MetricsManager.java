package fr.hyriode.hyreos.metrics;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyreos.Hyreos;
import fr.hyriode.hyreos.metrics.data.IHyreosMetric;
import fr.hyriode.hyreos.metrics.handler.MoneyMetricsHandler;
import fr.hyriode.hyreos.metrics.handler.NetworkMetricsHandler;
import fr.hyriode.hyreos.metrics.handler.PlayersMetricsHandler;
import fr.hyriode.hyreos.metrics.handler.ServiceMetricsHandler;
import fr.hyriode.hyreos.metrics.processor.IMetricHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class MetricsManager {

    private final Set<IMetricHandler> metrics = new HashSet<>();

    public void start() {
        this.metrics.add(new MoneyMetricsHandler());
        this.metrics.add(new NetworkMetricsHandler());
        this.metrics.add(new PlayersMetricsHandler());
        this.metrics.add(new ServiceMetricsHandler());

        HyriAPI.get().getScheduler().schedule(this::process, 60, 60, TimeUnit.SECONDS);
    }

    public void initialize() {
        final Stream<IMetricHandler> stream = this.metrics.stream().filter(IMetricHandler::isInitialized);
        final List<IHyriPlayer> players = HyriAPI.get().getPlayerManager().getPlayers();

        stream.forEach(handler -> handler.initialize(players));
    }

    public void process() {
        for (final IMetricHandler metric : this.metrics) {
            metric.process().forEach(this::save);
        }
    }

    private void save(IHyreosMetric metric) {
        Hyreos.get().getInflux().sendMetric(metric);
    }
}
