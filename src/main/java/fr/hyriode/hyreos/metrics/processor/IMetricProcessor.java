package fr.hyriode.hyreos.metrics.processor;

import fr.hyriode.hyreos.metrics.data.IHyreosMetric;

import java.util.Set;

@FunctionalInterface
public interface IMetricProcessor {

    Set<IHyreosMetric> process();
}
