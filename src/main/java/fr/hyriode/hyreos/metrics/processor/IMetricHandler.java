package fr.hyriode.hyreos.metrics.processor;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyreos.metrics.data.IHyreosMetric;

import java.util.List;
import java.util.Set;

public interface IMetricHandler {

    Set<IHyreosMetric> process();

    boolean isInitialized();
    void initialize(List<IHyriPlayer> players);
}
